package com.saas.springbackend.user.service;

import com.saas.springbackend.company.dtos.LoginRequest;
import com.saas.springbackend.company.dtos.LoginResponseDto;

import com.saas.springbackend.user.dto.UpdateProfileDTO;
import com.saas.springbackend.user.dto.UserRequestDTO;
import com.saas.springbackend.user.dto.UserResponseDTO;

import java.util.List;

public interface UserService {
  
    LoginResponseDto verify(LoginRequest request);

    // User Management
    UserResponseDTO addUser(UserRequestDTO requestDTO);

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO getUserById(Long userId);

    UserResponseDTO updateProfile(Long userId, UpdateProfileDTO dto);

    void deleteUser(Long userId);

    UserResponseDTO getCurrentUser();

    List<UserResponseDTO> getCompanyUsers();
}
