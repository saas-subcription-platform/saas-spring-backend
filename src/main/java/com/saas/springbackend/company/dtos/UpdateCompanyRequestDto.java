package com.saas.springbackend.company.dtos;

import com.saas.springbackend.company.entity.CompanySize;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCompanyRequestDto {

    private String phone;

    private String address;

    private String city;

    private String state;

    private String country;

    private String zipCode;

    private String gstNumber;

    private CompanySize companySize;

}