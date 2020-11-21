package com.chrisgya.springsecurity.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Optional;
import java.util.Set;

@Data
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
    private String lastname;
    @NotBlank
    private String password;
    private Set<Long> roleIds;

    public Optional<Set<Long>> getRoleIds() {
        return Optional.ofNullable(roleIds);
    }
}
