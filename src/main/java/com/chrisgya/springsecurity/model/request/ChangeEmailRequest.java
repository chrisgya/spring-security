package com.chrisgya.springsecurity.model.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class ChangeEmailRequest {
    @NotBlank
    @Email
    @Size(max = 75)
    private String email;
}
