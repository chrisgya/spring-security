package com.chrisgya.springsecurity.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class ChangeUsernameRequest {
    @NotBlank
    @Size(min=3, max = 50)
    private String username;
}
