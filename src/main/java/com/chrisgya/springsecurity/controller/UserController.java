package com.chrisgya.springsecurity.controller;

import com.chrisgya.springsecurity.entity.Permission;
import com.chrisgya.springsecurity.entity.Role;
import com.chrisgya.springsecurity.entity.User;
import com.chrisgya.springsecurity.model.UserDetailsImpl;
import com.chrisgya.springsecurity.model.request.ChangeEmailRequest;
import com.chrisgya.springsecurity.model.request.ChangePasswordRequest;
import com.chrisgya.springsecurity.model.request.ChangeUsernameRequest;
import com.chrisgya.springsecurity.model.request.UpdateUserRequest;
import com.chrisgya.springsecurity.service.userService.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Tag(name = "User management", description = "Manages user management")
@SecurityRequirement(name = "api")
@RestController
@RequestMapping("api/v1/users")
@Validated
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "get my details", description = "Retrieve current login user's details")
    @GetMapping("me")
    @ResponseStatus(HttpStatus.OK)
    public UserDetailsImpl getCurrentUser() {
        return userService.getCurrentUser();
    }

    @Operation(summary = "get my roles", description = "Retrieve current login user's roles")
    @GetMapping("me/roles")
    @ResponseStatus(HttpStatus.OK)
    public Set<Role> getCurrentUserRoles() {
        return userService.getCurrentUserRoles();
    }

    @Operation(summary = "get my permissions", description = "Retrieve current login user's permissions")
    @GetMapping("me/permissions")
    @ResponseStatus(HttpStatus.OK)
    public List<Permission> getCurrentUserPermissions() {
        return userService.getCurrentUserPermissions();
    }

    @Operation(summary = "change my password", description = "change my current password using my current login credentials")
    @PostMapping("change-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@Valid @RequestBody ChangePasswordRequest req) {
        userService.changePassword(req);
    }

    @Operation(summary = "change my email", description = "change email using my current login credentials. changing email requires you to reconfirm your account")
    @PostMapping("change-email")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@Valid @RequestBody ChangeEmailRequest req) {
        userService.changeEmail(req);
    }

    @Operation(summary = "change my username", description = "change username using my current login credentials.")
    @PostMapping("change-username")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@Valid @RequestBody ChangeUsernameRequest req) {
        userService.changeUsername(req);
    }

    @Operation(summary = "update my details", description = "update some of my personal details")
    @PutMapping("me")
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@Valid @RequestBody UpdateUserRequest req) {
        return userService.updateUser(req);
    }

    @Operation(summary = "change my passport photo", description = "set new passport photo")
    @PostMapping("me/photo")
    @ResponseStatus(HttpStatus.OK)
    public User updateUserPicture(@io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/octet-stream", schema = @Schema(type = "string", format = "binary"))) @RequestParam(name = "photo") MultipartFile photo
    ) {
        return userService.updateUserPicture(photo);
    }

}
