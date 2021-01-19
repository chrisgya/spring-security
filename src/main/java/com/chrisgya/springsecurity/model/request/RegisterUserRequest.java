package com.chrisgya.springsecurity.model.request;

import com.chrisgya.springsecurity.utils.validations.FieldsValueMatch;
import com.chrisgya.springsecurity.utils.validations.Password;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Optional;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@FieldsValueMatch(field = "password", fieldMatch = "confirmPassword", message = "password and confirmPassword do not match")
public class RegisterUserRequest {
    @NotBlank
    @Size(min=3, max = 50)
    private String username;

    @NotBlank
    @Email
    @Size(max = 100)
    private String email;

    @NotBlank
    @Size(min=3, max = 50)
    private String firstName;

    @Size(min=3, max = 50)
    private String middleName;

    @NotBlank
    @Size(min=3, max = 50)
    private String lastName;

    @Password
    private String password;

    private String confirmPassword;

    private Set<Long> roleIds;

    public Optional<Set<Long>> getRoleIds() {
        return Optional.ofNullable(roleIds);
    }
}
