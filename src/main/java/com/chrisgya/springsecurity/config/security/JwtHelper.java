package com.chrisgya.springsecurity.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.chrisgya.springsecurity.config.properties.JwtProperties;
import com.chrisgya.springsecurity.model.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtHelper {
private final JwtProperties jwtProperties;
    private final RSAPrivateKey privateKey;
    private final RSAPublicKey publicKey;

    public String createJwtForClaims(UserDetailsImpl userDetails) {

        Map<String, String> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());

        List<String> authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        claims.put("userId", String.valueOf(userDetails.getId()));
        claims.put("firstName", userDetails.getFirstName());
        claims.put("middleName", userDetails.getMiddleName());
        claims.put("lastname", userDetails.getLastname());

        JWTCreator.Builder jwtBuilder = JWT.create().withSubject(userDetails.getEmail()).withClaim("authorities", authorities);

        // Add claims
        claims.forEach(jwtBuilder::withClaim);

        // Add expiredAt and etc
        return jwtBuilder
                .withJWTId(UUID.randomUUID().toString())
                .withIssuer(jwtProperties.getIssuer())
                .withNotBefore(new Date())
                .withExpiresAt(Date.from(Instant.now().plusSeconds(jwtProperties.getTokenExpirationAfterSeconds())))
                .sign(Algorithm.RSA256(publicKey, privateKey));
    }

}
