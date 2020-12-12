package com.chrisgya.springsecurity.service;

import com.chrisgya.springsecurity.entity.User;
import com.chrisgya.springsecurity.model.LoginRequest;
import com.chrisgya.springsecurity.model.RegisterUserRequest;
import com.chrisgya.springsecurity.model.UserPage;
import com.chrisgya.springsecurity.model.UserParameters;
import org.springframework.data.domain.Page;

public interface UserService {
    String login(LoginRequest req);

    User registerUser(RegisterUserRequest req);

    User getUser(Long id);

    User getUserByEmail(String email);

    Page<User> getUsers(UserParameters params, UserPage userPage);
}
