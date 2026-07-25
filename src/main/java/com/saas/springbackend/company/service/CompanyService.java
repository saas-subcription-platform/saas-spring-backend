package com.saas.springbackend.company.service;

import com.saas.springbackend.company.dtos.CompanyRequestDto;

public interface CompanyService {
    public void registerCompany(CompanyRequestDto companyRequestDto);
}
