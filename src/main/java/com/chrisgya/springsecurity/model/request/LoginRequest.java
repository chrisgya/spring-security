package com.chrisgya.springsecurity.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginRequest {

    @Schema(description = "Registered email address", example = "admin@chrisgya.com", required = true)
    @NotBlank
    private String email;

    @Schema(description = "User password", example = "Password@1", required = true)
    @NotBlank
    private String password;
}
