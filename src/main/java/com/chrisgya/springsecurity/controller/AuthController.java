package com.chrisgya.springsecurity.controller;

import com.chrisgya.springsecurity.config.properties.JwtProperties;
import com.chrisgya.springsecurity.model.RegisterUserRequest;
import com.chrisgya.springsecurity.model.request.LoginRequest;
import com.chrisgya.springsecurity.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserServiceImpl userService;
    private final JwtProperties jwtProperties;

    @PostMapping("/signin")
    @ResponseStatus(HttpStatus.OK)
    public void authenticateUser(@Valid @RequestBody LoginRequest req, HttpServletResponse res) {
        var result = userService.login(req);
        res.addHeader("Authorization", jwtProperties.getTokenPrefix() + result.getAccessToken());
        res.addHeader("Refresh-Token", result.getRefreshToken());
    }

    @GetMapping("/refresh-token/{token}")
    @ResponseStatus(HttpStatus.OK)
    public void refreshToken(@PathVariable String token, HttpServletResponse res) {
        var result = userService.refreshToken(token);
        res.addHeader("Authorization", jwtProperties.getTokenPrefix() + result.getAccessToken());
        res.addHeader("Refresh-Token", result.getRefreshToken());
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterUserRequest req) {
        var response = userService.registerUser(req);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/verify-account/{token}")
    @ResponseStatus(HttpStatus.OK)
    public void verifyAccount(@PathVariable String token) {
        userService.verifyAccount(token);
    }

}
