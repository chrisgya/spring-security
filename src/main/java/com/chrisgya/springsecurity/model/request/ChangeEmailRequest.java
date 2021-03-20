package com.chrisgya.springsecurity.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class ChangeEmailRequest {

    @Schema(description = "Registered email address", example = "admin@chrisgya.com", required = true)
    @NotBlank
    @Email
    @Size(max = 75)
    private String email;
}
