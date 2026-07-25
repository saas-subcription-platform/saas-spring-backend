package com.saas.springbackend.company.service;

import com.saas.springbackend.company.dtos.CompanyRequestDto;
import com.saas.springbackend.company.entity.Company;
import com.saas.springbackend.company.repository.CompanyRepository;
import com.saas.springbackend.user.entity.Role;
import com.saas.springbackend.user.entity.User;
import com.saas.springbackend.user.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyServiceImpl implements CompanyService{
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public void registerCompany(CompanyRequestDto companyRequestDto) {
        Company c = mapper.map(companyRequestDto, Company.class);
        User u = mapper.map(companyRequestDto, User.class);
        u.setRole(Role.ADMIN);
        u.setCompany(c);

        companyRepository.save(c);
        userRepository.save(u);
    }
}
