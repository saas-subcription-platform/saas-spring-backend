package com.saas.springbackend.security.password.service;

import com.saas.springbackend.common.exception.InvalidResetTokenException;
import com.saas.springbackend.common.exception.TokenExpiredException;
import com.saas.springbackend.common.mail.MailService;
import com.saas.springbackend.security.password.dto.ForgotPasswordRequestDto;
import com.saas.springbackend.security.password.dto.ResetPasswordRequestDto;
import com.saas.springbackend.security.password.entity.PasswordResetToken;
import com.saas.springbackend.security.password.repository.PasswordResetTokenRepository;
import com.saas.springbackend.user.entity.User;
import com.saas.springbackend.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final PasswordResetTokenRepository tokenRepository;

    private final MailService mailService;

    @Override
    public boolean validateResetToken(String token) {

        try {
            getValidToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void resetPassword(ResetPasswordRequestDto request) {

        PasswordResetToken resetToken =
                getValidToken(request.getToken());

        User user = resetToken.getUser();

        user.setPassword(
                passwordEncoder.encode(request.getNewPassword())
        );

        userRepository.save(user);

        tokenRepository.delete(resetToken);
    }


    private PasswordResetToken getValidToken(String token) {

        PasswordResetToken resetToken = tokenRepository
                .findByToken(token)
                .orElseThrow(() ->
                        new InvalidResetTokenException("Invalid token"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Reset token has expired");
        }

        return resetToken;
    }

    @Override
    public void forgotPassword(ForgotPasswordRequestDto request) {

        System.out.println("Step 1");

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Person not found"));

        System.out.println("Step 2");

        tokenRepository.findByUser_Id(user.getId())
                .ifPresent(tokenRepository::delete);

        System.out.println("Step 3");

        String token = UUID.randomUUID().toString();

        System.out.println("Step 4");

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(15));

        tokenRepository.save(resetToken);

        System.out.println("Step 5");

        String resetLink = "http://localhost:5173/reset-password?token=" + token;

        String body = """
        Hello,

        To reset your password, please click the link below:

        %s

        If you did not request a password reset, you can safely ignore this email.

        This link will expire in 15 minutes.

        Regards,
        SaaS Subscription Platform Team
        """.formatted(resetLink);

        mailService.sendEmail(
                user.getEmail(),
                "Password Reset",
                body
        );

        System.out.println("Step 6");
    }
}

