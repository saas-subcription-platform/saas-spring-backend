package com.saas.springbackend.company.service;

import com.saas.springbackend.company.dtos.CompanyProfileResponseDto;
import com.saas.springbackend.company.dtos.CompanyRequestDto;
import com.saas.springbackend.company.dtos.LoginRequest;
import com.saas.springbackend.company.dtos.UpdateCompanyRequestDto;
import com.saas.springbackend.dashboard.dto.DashboardResponseDto;
import com.saas.springbackend.user.entity.User;

public interface CompanyService {
    public void registerCompany(CompanyRequestDto companyRequestDto);
    CompanyProfileResponseDto getCompanyProfile();
    void updateCompanyProfile(UpdateCompanyRequestDto requestDto);
    DashboardResponseDto getDashboardDetails();
}
