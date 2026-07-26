package com.saas.springbackend.company.service;

import com.saas.springbackend.common.exception.CompanyAlreadyExistsException;
import com.saas.springbackend.common.exception.EmailAlreadyExistsException;
import com.saas.springbackend.company.dtos.CompanyRequestDto;
import com.saas.springbackend.company.dtos.LoginRequest;
import com.saas.springbackend.company.entity.Company;
import com.saas.springbackend.company.repository.CompanyRepository;
import com.saas.springbackend.security.jwt.JwtService;
import com.saas.springbackend.user.entity.Role;
import com.saas.springbackend.user.entity.User;
import com.saas.springbackend.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService{

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

//    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Override
    @Transactional
    public void registerCompany(CompanyRequestDto companyRequestDto) {
        if (userRepository.existsByEmail(companyRequestDto.getEmail().toLowerCase().trim())) {
            throw new EmailAlreadyExistsException(companyRequestDto.getEmail()+": Email already exists");
        }

        if (companyRepository.existsByCompanyName(companyRequestDto.getCompanyName())) {
            throw new CompanyAlreadyExistsException(companyRequestDto.getCompanyName()+": Company name already exists");
        }

        Company c = mapper.map(companyRequestDto, Company.class);
        User u = mapper.map(companyRequestDto, User.class);
        u.setEmail(u.getEmail().toLowerCase().trim());
        u.setPassword(passwordEncoder.encode(companyRequestDto.getPassword()));
        u.setRole(Role.ADMIN);
        u.setCompany(c);

        companyRepository.save(c);
        userRepository.save(u);
    }
}
