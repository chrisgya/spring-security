package com.chrisgya.springsecurity.controller;

import com.chrisgya.springsecurity.config.properties.JwtProperties;
import com.chrisgya.springsecurity.model.LoginRequest;
import com.chrisgya.springsecurity.model.RegisterUserRequest;
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
    public void authenticateUser(@RequestBody LoginRequest loginRequest, HttpServletResponse res) {
        res.addHeader("Authorization", jwtProperties.getTokenPrefix() + userService.login(loginRequest));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterUserRequest req) {
        var response = userService.registerUser(req);
        return ResponseEntity.ok(response);
    }

}
