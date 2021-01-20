package com.chrisgya.springsecurity.service;

import com.chrisgya.springsecurity.entity.User;
import com.chrisgya.springsecurity.model.AuthenticationResponse;
import com.chrisgya.springsecurity.model.UserDetailsImpl;
import com.chrisgya.springsecurity.model.UserPage;
import com.chrisgya.springsecurity.model.UserParameters;
import com.chrisgya.springsecurity.model.request.ChangePasswordRequest;
import com.chrisgya.springsecurity.model.request.LoginRequest;
import com.chrisgya.springsecurity.model.request.RegisterUserRequest;
import com.chrisgya.springsecurity.model.request.ResetPasswordRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    AuthenticationResponse login(LoginRequest req);

    AuthenticationResponse refreshToken(String refreshToken);

    User registerUser(RegisterUserRequest req);

    void requestConfirmationLink(String email);

    void verifyAccount(String token);

    void forgottenPassword(String email);

    void resetPassword(String token, ResetPasswordRequest req);

    void changePassword(ChangePasswordRequest req);

    UserDetailsImpl getCurrentUser();

    User getUser(Long id);

    User getUserByEmail(String email);

    Page<User> getUsers(UserParameters params, UserPage userPage);

    List<User> getUsers();
}
