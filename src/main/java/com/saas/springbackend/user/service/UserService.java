package com.saas.springbackend.user.service;

import com.saas.springbackend.company.dtos.LoginRequest;
import com.saas.springbackend.company.dtos.LoginResponseDto;

public interface UserService {
    LoginResponseDto verify(LoginRequest request);
}
