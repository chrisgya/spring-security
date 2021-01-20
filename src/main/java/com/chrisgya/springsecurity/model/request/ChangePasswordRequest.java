package com.chrisgya.springsecurity.model.request;

import com.chrisgya.springsecurity.utils.validations.FieldsValueMatch;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@FieldsValueMatch(field = "newPassword", fieldMatch = "confirmPassword", message = "newPassword and confirmPassword do not match")
public class ChangePasswordRequest {
    @NotBlank
    private String password;
    @NotBlank
    private String newPassword;
    private String confirmPassword;
}
