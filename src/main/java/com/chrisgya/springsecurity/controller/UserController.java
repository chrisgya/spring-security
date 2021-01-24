package com.chrisgya.springsecurity.controller;

import com.chrisgya.springsecurity.entity.User;
import com.chrisgya.springsecurity.model.UserDetailsImpl;
import com.chrisgya.springsecurity.model.request.ChangeEmailRequest;
import com.chrisgya.springsecurity.model.request.ChangePasswordRequest;
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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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

    @PutMapping("me")
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@RequestParam @NotBlank @Size(min = 3, max = 50) String firstName,
                           @RequestParam(required = false) @Size(min = 3, max = 50) String middleName,
                           @RequestParam @NotBlank @Size(min = 3, max = 50) String lastName,
                           @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/octet-stream", schema = @Schema(type = "string", format = "binary"))) @RequestParam(name = "picture", required = false) MultipartFile picture
    ) {
      return  userService.updateUser(UpdateUserRequest.builder()
                .firstName((firstName))
                .middleName(middleName)
                .lastName(lastName)
                .picture(picture)
                .build());
    }

}
