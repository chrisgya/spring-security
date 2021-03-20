package com.chrisgya.springsecurity.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UpdatePermissionRequest {
    @Schema(description = "Permission name", example = "can_read_users", required = true)
    @NotBlank
    @Size(min=3, max = 50)
    private String name;

    @Schema(description = "Permission description", example = "Roles with this permission can view any user's information")
    @Size(min=3, max = 100)
    private String description;
}