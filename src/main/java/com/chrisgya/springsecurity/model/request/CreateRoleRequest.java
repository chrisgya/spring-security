package com.chrisgya.springsecurity.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "Role name", example = "administrator", required = true)
    @NotBlank
    @Size(min=3, max = 50)
    private String name;

    @Schema(description = "Role description", example = "administrator role can manage all entities")
    @Size(min=3, max = 100)
    private String description;

    @Schema(description = "List of permission IDs assigned to this role. NB: (-1) means all permissions should be assigned to this role", example = "[1, 2, 3]", required = true)
    @NotNull
    private Set<Long> permissionIds;
}