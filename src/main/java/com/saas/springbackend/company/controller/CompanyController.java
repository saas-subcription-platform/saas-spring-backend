package com.saas.springbackend.company.controller;

import com.saas.springbackend.company.dtos.CompanyRequestDto;
import com.saas.springbackend.company.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ResponseBody
public class CompanyController {
    @Autowired
    private CompanyService companyService;

    @PostMapping("/register")
    public void registerCompany(@RequestBody CompanyRequestDto requestDto) {
        System.out.println("In the register controller");
        System.out.println(requestDto);
        companyService.registerCompany(requestDto);
    }
}
