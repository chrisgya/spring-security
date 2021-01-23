package com.chrisgya.springsecurity.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UpdateUserRequest {

    @NotBlank
    @Size(min=3, max = 50)
    private String firstName;

    @Size(min=3, max = 50)
    private String middleName;

    @NotBlank
    @Size(min=3, max = 50)
    private String lastName;

    private MultipartFile picture;
}
