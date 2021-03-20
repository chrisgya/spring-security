package com.chrisgya.springsecurity.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class ChangeUsernameRequest {

    @Schema(description = "username", example = "JohnDoe", required = true)
    @NotBlank
    @Size(min=3, max = 50)
    private String username;
}
