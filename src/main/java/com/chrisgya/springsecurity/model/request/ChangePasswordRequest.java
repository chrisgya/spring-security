package com.chrisgya.springsecurity.model.request;

import com.chrisgya.springsecurity.utils.validations.FieldsValueMatch;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@FieldsValueMatch(field = "newPassword", fieldMatch = "confirmPassword", message = "newPassword and confirmPassword do not match")
public class ChangePasswordRequest {
    @Schema(description = "Current password to be changed", example = "Joh#4554Cool", required = true)
    @NotBlank
    private String password;

    @Schema(description = "New password", example = "Password@1", required = true)
    @NotBlank
    private String newPassword;

    @Schema(description = "Should be same as newPassword", example = "Password@1", required = true)
    @NotBlank
    private String confirmPassword;
}
