package com.saas.springbackend.security.password.controller;

import com.saas.springbackend.security.password.dto.ForgotPasswordRequestDto;
import com.saas.springbackend.security.password.dto.ResetPasswordRequestDto;
import com.saas.springbackend.security.password.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @RequestBody ForgotPasswordRequestDto request) {

        passwordResetService.forgotPassword(request);

        return ResponseEntity.ok("Password reset link has been sent to your email.");
    }

    @GetMapping("/validate-reset-token")
    public ResponseEntity<String> validateResetToken(
            @RequestParam String token) {

        boolean isValid = passwordResetService.validateResetToken(token);

        if (isValid) {
            return ResponseEntity.ok("Valid Token");
        }

        return ResponseEntity.badRequest()
                .body("Invalid or Expired Token");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestBody ResetPasswordRequestDto request) {

        passwordResetService.resetPassword(request);

        return ResponseEntity.ok("Password reset successfully.");
    }
}