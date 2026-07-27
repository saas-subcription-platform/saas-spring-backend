package com.saas.springbackend.user.service;

import com.saas.springbackend.company.dtos.LoginRequest;
import com.saas.springbackend.company.dtos.LoginResponseDto;
import com.saas.springbackend.security.jwt.JwtService;
import com.saas.springbackend.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public LoginResponseDto verify(LoginRequest request) {

        System.out.println("Email received: " + request.getEmail());

        boolean exists = userRepository.existsByEmail(request.getEmail());

        System.out.println("Exists: " + exists);

        if (!exists)
        {
            System.out.println("excpetion thrown");
            throw new BadCredentialsException("Invalid Email Or Password");
        }

            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()));

            System.out.println("Authenticated: " + auth.isAuthenticated());

            String token = jwtService.generateToken(request.getEmail());
            String role = auth.getAuthorities()
                    .iterator()
                    .next()
                    .getAuthority()
                    .replace("ROLE_", "");

            return new LoginResponseDto(token, role);



    }

}
