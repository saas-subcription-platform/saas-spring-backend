package com.saas.springbackend.company.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class LoginResponseDto {

    private String token;
    private String role;

}