package com.chrisgya.springsecurity.service;

import com.chrisgya.springsecurity.AbstractIT;
import com.chrisgya.springsecurity.model.request.RegisterUserRequest;
import com.chrisgya.springsecurity.service.userService.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.chrisgya.springsecurity.FakerUtil.*;
import static org.assertj.core.api.Assertions.assertThat;


class UserServiceImplTest extends AbstractIT {

    @Autowired
    private UserService userService;

    @Test
    void registerUser() {
        RegisterUserRequest req = RegisterUserRequest.builder()
                .username(getUsername())
                .email(getEmailAddress())
                .firstName(getFirstName())
                .mobileNo(getPhoneNumber())
                .middleName(null)
                .lastName(getLastName())
                .password("Password@1")
                .confirmPassword("Password@1")
                .build();
       var user = userService.registerUser(req);

       assertThat(user.getId()).isNotNull();
       assertThat(user.getUsername()).isEqualTo(req.getUsername());
    }
}