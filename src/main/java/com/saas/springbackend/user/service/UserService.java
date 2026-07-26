package com.saas.springbackend.user.service;

import com.saas.springbackend.company.dtos.LoginRequest;

public interface UserService {
    String verify(LoginRequest request);
}
