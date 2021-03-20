package com.chrisgya.springsecurity.model.request;

import com.chrisgya.springsecurity.utils.validations.FieldsValueMatch;
import com.chrisgya.springsecurity.utils.validations.Password;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@FieldsValueMatch(field = "password", fieldMatch = "confirmPassword", message = "password and confirmPassword do not match")
public class ResetPasswordRequest {
    @Schema(description = "User password", example = "Password@1", required = true)
    @Password
    private String password;

    @Schema(description = "Confirm password should be the same as user password", example = "Password@1", required = true)
    @NotBlank
    private String confirmPassword;

}
