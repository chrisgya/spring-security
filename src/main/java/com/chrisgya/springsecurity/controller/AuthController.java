package com.chrisgya.springsecurity.controller;

import com.chrisgya.springsecurity.config.properties.JwtProperties;
import com.chrisgya.springsecurity.model.AuthenticationResponse;
import com.chrisgya.springsecurity.model.request.LoginRequest;
import com.chrisgya.springsecurity.model.request.RegisterUserRequest;
import com.chrisgya.springsecurity.model.request.ResetPasswordRequest;
import com.chrisgya.springsecurity.service.userService.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserServiceImpl userService;
    private final JwtProperties jwtProperties;

//    @PostMapping("/signin")
//    @ResponseStatus(HttpStatus.OK)
//    public void authenticateUser(@Valid @RequestBody LoginRequest req, HttpServletResponse res) {
//        var result = userService.login(req);
//        res.addHeader("Authorization", jwtProperties.getTokenPrefix() + result.getAccessToken());
//        res.addHeader("Refresh-Token", result.getRefreshToken());
//    }

    @PostMapping("login")
    @ResponseStatus(HttpStatus.OK)
    public AuthenticationResponse authenticateUser(@Valid @RequestBody LoginRequest req) {
        return userService.login(req);
    }

    @GetMapping("refresh-token/{token}")
    @ResponseStatus(HttpStatus.OK)
    public AuthenticationResponse refreshToken(@PathVariable String token) {
        return userService.refreshToken(token);
    }

    @GetMapping("username-exist/{username}")
    @ResponseStatus(HttpStatus.OK)
    public boolean checkIfUsernameExist(@PathVariable String username) {
        return userService.checkIfUsernameExist(username);
    }

    @GetMapping("email-exist/{email}")
    @ResponseStatus(HttpStatus.OK)
    public boolean checkIfEmailExist(@PathVariable String email) {
        return userService.checkIfEmailExist(email);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterUserRequest req) {
        return ResponseEntity.ok(userService.registerUser(req));
    }

    @PutMapping("/request-confirmation-link/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void requestConfirmationLink(@PathVariable String email) {
        userService.requestConfirmationLink(email);
    }

    @PutMapping("/verify-account/{token}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void verifyAccount(@PathVariable String token) {
        userService.verifyAccount(token);
    }

    @PutMapping("/forgot-password/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void forgottenPassword(@PathVariable String email) {
        userService.forgottenPassword(email);
    }

    @PutMapping("/reset-password/{token}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resetPassword(@PathVariable String token, @Valid @RequestBody ResetPasswordRequest req) {
        userService.resetPassword(token, req);
    }

}
