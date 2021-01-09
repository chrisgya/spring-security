package com.chrisgya.springsecurity.service;

import com.chrisgya.springsecurity.entity.User;
import com.chrisgya.springsecurity.model.AuthenticationResponse;
import com.chrisgya.springsecurity.model.RegisterUserRequest;
import com.chrisgya.springsecurity.model.UserPage;
import com.chrisgya.springsecurity.model.UserParameters;
import com.chrisgya.springsecurity.model.request.LoginRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    AuthenticationResponse login(LoginRequest req);

    AuthenticationResponse refreshToken(String refreshToken);

    User registerUser(RegisterUserRequest req);

    void verifyAccount(String token);

    User getCurrentUser();

    User getUser(Long id);

    User getUserByEmail(String email);

    Page<User> getUsers(UserParameters params, UserPage userPage);

    List<User> getUsers();
}
