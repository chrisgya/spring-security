package com.chrisgya.springsecurity.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Validated
@Data
@ConstructorBinding
@ConfigurationProperties("file")
public class FileProperties {
    @NotBlank
    private final String directory;
    @NotBlank
    private final String imageExtensions;
    @NotBlank
    private final String fileExtensions;
    @NotNull
    private final Integer maxImageSize;
}