package com.chrisgya.springsecurity.model;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
