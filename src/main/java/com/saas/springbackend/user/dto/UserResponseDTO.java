package com.saas.springbackend.user.dto;

import com.saas.springbackend.user.entity.Role;
import com.saas.springbackend.user.entity.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private Long userId;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String department;

    private Role role;

    private UserStatus status;

    private Long companyId;

    private String companyName;
}