package com.chrisgya.springsecurity.model.request;

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
public class CreatePermissionRequest {
    @NotBlank
    @Size(min=3, max = 50)
    private String name;

    @Size(max = 100)
    private String description;
}