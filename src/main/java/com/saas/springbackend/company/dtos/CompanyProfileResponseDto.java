package com.saas.springbackend.company.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyProfileResponseDto {

    private Long company_id;

    private String name;

    private String email;

    private String companyName;

    private String phone;

    private String address;

    private String city;

    private String state;

    private String country;

    private String zipCode;

    private String gstNumber;

    private String companySize;

}