package com.chrisgya.springsecurity.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CreateRoleRequest {
    @NotBlank
    @Size(min=3, max = 50)
    private String name;

    @Size(min=3, max = 100)
    private String description;

    @NotNull
    private Set<Long> permissionIds;
}