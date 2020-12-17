package com.chrisgya.springsecurity.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
@Data
@ConstructorBinding
@ConfigurationProperties("custom-thread")
public class CustomThreadProperties {
    @NotNull
    private final int corePoolSize;
    @NotNull
    private final int maxPoolSize;
    @NotNull
    private final int queueCapacity;
}
