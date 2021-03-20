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
public class UpdateUserRequest {

    @Schema(description = "User first name", example = "John", required = true)
    @NotBlank
    @Size(min=3, max = 50)
    private String firstName;

    @Schema(description = "User middle name", example = "Kwesi")
    @Size(min=3, max = 50)
    private String middleName;

    @Schema(description = "User last name", example = "Doe", required = true)
    @NotBlank
    @Size(min=3, max = 50)
    private String lastName;
}
