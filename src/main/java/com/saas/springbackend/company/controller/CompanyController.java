package com.saas.springbackend.company.controller;

import com.saas.springbackend.company.dtos.*;
import com.saas.springbackend.company.service.CompanyService;
import com.saas.springbackend.user.entity.User;
import com.saas.springbackend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    public LoginResponseDto loginCompany(@RequestBody LoginRequest request)  {

            try {
                return userService.verify(request);
            } catch (BadCredentialsException e) {
                throw new  BadCredentialsException(e.getMessage());
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }

    }

    @GetMapping("/admin/company")
    public ResponseEntity<CompanyProfileResponseDto> getCompanyProfile() {

        return ResponseEntity.ok(companyService.getCompanyProfile());

    }

    @PutMapping("/admin/company/edit")
    public ResponseEntity<String> updateCompanyProfile(
            @RequestBody UpdateCompanyRequestDto requestDto) {

        companyService.updateCompanyProfile(requestDto);

        return ResponseEntity.ok("Company profile updated successfully");
    }
}
