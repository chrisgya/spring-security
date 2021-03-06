package com.chrisgya.springsecurity.controller;

import com.chrisgya.springsecurity.config.properties.JwtProperties;
import com.chrisgya.springsecurity.entity.User;
import com.chrisgya.springsecurity.model.AuthenticationResponse;
import com.chrisgya.springsecurity.model.request.LoginRequest;
import com.chrisgya.springsecurity.model.request.RegisterUserRequest;
import com.chrisgya.springsecurity.model.request.ResetPasswordRequest;
import com.chrisgya.springsecurity.service.userService.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "User Account management", description = "Manages user account such as user and password management")
@SecurityRequirement(name = "api")
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
    @Operation(summary = "create new account", description = "This will create a new user and send send account confirmation email to the provided email address")
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.OK)
    public User registerUser(@Valid @RequestBody RegisterUserRequest req) {
        return userService.registerUser(req);
    }

    @Operation(summary = "login", description = "login with your registered email and password to obtain access and refresh tokens")
    @PostMapping("login")
    @ResponseStatus(HttpStatus.OK)
    public AuthenticationResponse authenticateUser(@Valid @RequestBody LoginRequest req) {
        return userService.login(req);
    }

    @Operation(summary = "request access token with refresh token", description = "use the existing refresh token obtained when you login to get a new access token")
    @GetMapping("refresh-token/{token}")
    @ResponseStatus(HttpStatus.OK)
    public AuthenticationResponse refreshToken(@PathVariable String token) {
        return userService.refreshToken(token);
    }

    @Operation(summary = "check username", description = "check if username exists")
    @GetMapping("username-exist/{username}")
    @ResponseStatus(HttpStatus.OK)
    public boolean checkIfUsernameExist(@PathVariable String username) {
        return userService.checkIfUsernameExist(username);
    }

    @Operation(summary = "check email", description = "check if email exists")
    @GetMapping("email-exist/{email}")
    @ResponseStatus(HttpStatus.OK)
    public boolean checkIfEmailExist(@PathVariable String email) {
        return userService.checkIfEmailExist(email);
    }

    @Operation(summary = "request new confirmation token", description = "This endpoint will send new account activation token to the provided email if is already registered and not confirmed already")
    @PutMapping("/request-confirmation-link/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void requestConfirmationLink(@PathVariable String email) {
        userService.requestConfirmationLink(email);
    }

    @Operation(summary = "verify account", description = "use the token sent to your registered email to confirm the account")
    @PutMapping("/verify-account/{token}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void verifyAccount(@PathVariable String token) {
        userService.verifyAccount(token);
    }

    @Operation(summary = "request password reset link", description = "password reset link would be sent to the registered email address")
    @PutMapping("/forgot-password/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void forgottenPassword(@PathVariable String email) {
        userService.forgottenPassword(email);
    }

    @Operation(summary = "set a new password with token", description = "use the token sent to your registered email to set new password")
    @PutMapping("/reset-password/{token}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resetPassword(@PathVariable String token, @Valid @RequestBody ResetPasswordRequest req) {
        userService.resetPassword(token, req);
    }

}
