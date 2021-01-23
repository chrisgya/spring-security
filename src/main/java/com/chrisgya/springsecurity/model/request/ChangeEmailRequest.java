package com.chrisgya.springsecurity.model.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class ChangeEmailRequest {
    @NotBlank
    @Email
    private String email;
}
