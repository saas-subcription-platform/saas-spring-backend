package com.saas.springbackend.company.service;

import com.saas.springbackend.common.exception.CompanyAlreadyExistsException;
import com.saas.springbackend.common.exception.EmailAlreadyExistsException;
import com.saas.springbackend.company.dtos.CompanyProfileResponseDto;
import com.saas.springbackend.company.dtos.CompanyRequestDto;
import com.saas.springbackend.company.dtos.LoginRequest;
import com.saas.springbackend.company.dtos.UpdateCompanyRequestDto;
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
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Override
    public CompanyProfileResponseDto getCompanyProfile() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Company company = user.getCompany();

        CompanyProfileResponseDto response = new CompanyProfileResponseDto();
        response.setCompany_id(company.getId());
        response.setName(user.getFirstName() + " " + user.getLastName());
        response.setEmail(user.getEmail());
        response.setCompanyName(company.getCompanyName());
        response.setPhone(company.getPhone());
        response.setAddress(company.getAddress());
        response.setCity(company.getCity());
        response.setState(company.getState());
        response.setCountry(company.getCountry());
        response.setZipCode(company.getZipCode());
        response.setGstNumber(company.getGstNumber());
        response.setCompanySize(company.getCompanySize().name());

        return response;
    }

    @Override
    @Transactional
    public void updateCompanyProfile(UpdateCompanyRequestDto requestDto) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Company company = user.getCompany();

        company.setPhone(requestDto.getPhone());
        company.setGstNumber(requestDto.getGstNumber());
        company.setCompanySize(requestDto.getCompanySize());

        company.setAddress(requestDto.getAddress());
        company.setCity(requestDto.getCity());
        company.setState(requestDto.getState());
        company.setCountry(requestDto.getCountry());
        company.setZipCode(requestDto.getZipCode());

        companyRepository.save(company);
    }
}
