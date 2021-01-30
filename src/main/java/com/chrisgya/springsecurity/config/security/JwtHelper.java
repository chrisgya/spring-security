package com.chrisgya.springsecurity.config.security;

import com.chrisgya.springsecurity.exception.ForbiddenException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class JwtHelper {
    private static final String CLIENT_ID = "client_id";
    private static final String CLIENT_NAME = "client_name";
    private static final String TOKEN_JTI = "jti";
    private static final String USERNAME = "user_name";
    private static final String EMAIL = "email";
    private static final String SCOPE = "scope";

    public String getClientId() {
        return getClaim(CLIENT_ID);
    }

    public String getClientName() {
        return getClaim(CLIENT_NAME);
    }

    public String getTokenJti() {
        return getClaim(TOKEN_JTI);
    }

    public String getUsername() {
        return getClaim(USERNAME);
    }

    public String getUserEmail() {
        return getClaim(EMAIL);
    }

    public String getTokenValue() {
        return getJwt().getTokenValue();
    }

    public String getAccessToken() {
        return "Bearer " + getJwt().getTokenValue();
    }

    public boolean hasScope(String scope) {
        return getClaimAsList(SCOPE).stream().anyMatch(s -> s.equalsIgnoreCase(scope));
    }

    private String getClaim(String key) {
        return getJwt().getClaimAsString(key);
    }

    private List<String> getClaimAsList(String key) {
        List<String> claim = getJwt().getClaimAsStringList(key);
        return Objects.nonNull(claim) ? claim : Collections.EMPTY_LIST;
    }

    private Jwt getJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(authentication) &&
                authentication.getPrincipal() instanceof Jwt) {
            return (Jwt) authentication.getPrincipal();
        }
        throw new ForbiddenException("Invalid authentication - authentication must not be null and must have a JWT principal");
    }
}