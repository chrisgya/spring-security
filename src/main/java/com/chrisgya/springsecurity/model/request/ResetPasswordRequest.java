package com.chrisgya.springsecurity.model.request;

import com.chrisgya.springsecurity.utils.validations.FieldsValueMatch;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@FieldsValueMatch(field = "password", fieldMatch = "confirmPassword", message = "password and confirmPassword do not match")
public class ResetPasswordRequest {
    @NotBlank
    private String password;
    private String confirmPassword;

}
