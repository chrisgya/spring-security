package com.chrisgya.springsecurity.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Validated
@Data
@ConstructorBinding
@ConfigurationProperties("front-end-url")
public class FrontEndUrlProperties {
    @NotBlank
    private final String confirmAccount;
    @NotBlank
    private final String resetPassword;
    @NotBlank
    private final String baseUrl;
}
