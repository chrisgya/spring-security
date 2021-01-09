package com.chrisgya.springsecurity.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpHeaders;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ConfigurationProperties(prefix = "app.security.jwt")
public class JwtProperties {
    @NotBlank
    private String keystoreLocation;
    @NotBlank
    private String keyStorePassword;
    @NotBlank
    private String keyAlias;
    @NotBlank
    private String privateKeyPassphrase;
    @NotBlank
    private String tokenPrefix;
    @NotBlank
    private String issuer;
    @NotNull
    private Integer bycriptStrength;
    @NotNull
    private Long tokenExpirationAfterSeconds;
    @NotNull
    private Long activationTokenExpirationAfterSeconds;
    @NotNull
    private Long refreshTokenExpiresAfterSeconds;

    public String getAuthorizationHeader() {
        return HttpHeaders.AUTHORIZATION;
    }
}
