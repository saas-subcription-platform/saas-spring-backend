package com.saas.springbackend.company.controller;

import com.saas.springbackend.company.dtos.CompanyRequestDto;
import com.saas.springbackend.company.service.CompanyService;
import com.saas.springbackend.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@ResponseBody
public class CompanyController {
    @Autowired
    private CompanyService companyService;

    @PostMapping("/register")
    public void registerCompany(@RequestBody CompanyRequestDto requestDto) {
        companyService.registerCompany(requestDto);
    }

    @PostMapping("/login")
    public String loginCompany(@RequestBody User u) {
        return companyService.verify(u);
    }

    @GetMapping("/admin/dashboard")
    public String dashboard() {
        return "This is Secured Page";
    }
}
