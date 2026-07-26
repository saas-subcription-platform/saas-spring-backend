package com.saas.springbackend.company.service;

import com.saas.springbackend.company.dtos.CompanyRequestDto;
import com.saas.springbackend.company.dtos.LoginRequest;
import com.saas.springbackend.user.entity.User;

public interface CompanyService {
    public void registerCompany(CompanyRequestDto companyRequestDto);

}
