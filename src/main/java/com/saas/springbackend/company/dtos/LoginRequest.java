package com.saas.springbackend.company.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
public class LoginRequest {

    private String email;

    private String password;

}
