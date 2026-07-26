package com.saas.springbackend.user.service;

import com.saas.springbackend.company.dtos.LoginRequest;
import com.saas.springbackend.security.jwt.JwtService;
import com.saas.springbackend.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
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
    public String verify(LoginRequest request) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()));

            System.out.println("Authenticated: " + auth.isAuthenticated());

            return jwtService.generateToken(request.getEmail());

        } catch (Exception e) {
            e.printStackTrace();
            return e.getClass().getSimpleName() + " : " + e.getMessage();
        }
    }

}
