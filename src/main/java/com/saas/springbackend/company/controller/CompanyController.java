package com.saas.springbackend.company.controller;

import com.saas.springbackend.company.dtos.CompanyRequestDto;
import com.saas.springbackend.company.dtos.LoginRequest;
import com.saas.springbackend.company.service.CompanyService;
import com.saas.springbackend.user.entity.User;
import com.saas.springbackend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@ResponseBody
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerCompany(@RequestBody CompanyRequestDto requestDto) {
        companyService.registerCompany(requestDto);
        return ResponseEntity.ok("Registration Successful");
    }

    @PostMapping("/login")
    public String loginCompany(@RequestBody LoginRequest request) {
        return userService.verify(request);
    }

    @GetMapping("/admin/dashboard")
    public String dashboard() {
        return "This is Secured Page";
    }
}
