package com.saas.springbackend.company.service;

import com.saas.springbackend.company.dtos.CompanyRequestDto;
import com.saas.springbackend.company.entity.Company;
import com.saas.springbackend.company.repository.CompanyRepository;
import com.saas.springbackend.security.jwt.JwtService;
import com.saas.springbackend.user.entity.Role;
import com.saas.springbackend.user.entity.User;
import com.saas.springbackend.user.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CompanyServiceImpl implements CompanyService{
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Override
    public void registerCompany(CompanyRequestDto companyRequestDto) {
        Company c = mapper.map(companyRequestDto, Company.class);
        User u = mapper.map(companyRequestDto, User.class);
        u.setPassword(encoder.encode(companyRequestDto.getPassword()));
        u.setRole(Role.ADMIN);
        u.setCompany(c);

        companyRepository.save(c);
        userRepository.save(u);
    }

    @Override
    public String verify(User u) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            u.getEmail(),
                            u.getPassword()));

            System.out.println("Authenticated: " + auth.isAuthenticated());

            return jwtService.generateToken(u.getEmail());

        } catch (Exception e) {
            e.printStackTrace();
            return e.getClass().getSimpleName() + " : " + e.getMessage();
        }
    }
}
