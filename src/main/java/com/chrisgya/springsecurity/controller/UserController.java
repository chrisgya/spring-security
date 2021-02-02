package com.chrisgya.springsecurity.controller;

import com.chrisgya.springsecurity.entity.User;
import com.chrisgya.springsecurity.model.UserDetailsImpl;
import com.chrisgya.springsecurity.model.request.ChangeEmailRequest;
import com.chrisgya.springsecurity.model.request.ChangePasswordRequest;
import com.chrisgya.springsecurity.model.request.ChangeUsernameRequest;
import com.chrisgya.springsecurity.model.request.UpdateUserRequest;
import com.chrisgya.springsecurity.service.userService.UserService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/users")
@Validated
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("me")
    @ResponseStatus(HttpStatus.OK)
    public UserDetailsImpl getCurrentUser() {
        return userService.getCurrentUser();
    }

    @PostMapping("change-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@Valid @RequestBody ChangePasswordRequest req) {
        userService.changePassword(req);
    }

    @PostMapping("change-email")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@Valid @RequestBody ChangeEmailRequest req) {
        userService.changeEmail(req);
    }

    @PostMapping("change-username")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@Valid @RequestBody ChangeUsernameRequest req) {
        userService.changeUsername(req);
    }

    @PutMapping("me")
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@Valid @RequestBody UpdateUserRequest req) {
        return userService.updateUser(req);
    }

    @PostMapping("me/photo")
    @ResponseStatus(HttpStatus.OK)
    public User updateUserPicture(@io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/octet-stream", schema = @Schema(type = "string", format = "binary"))) @RequestParam(name = "photo") MultipartFile photo
    ) {
        return userService.updateUserPicture(photo);
    }

}
