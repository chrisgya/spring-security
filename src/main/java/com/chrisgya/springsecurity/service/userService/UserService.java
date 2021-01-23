package com.chrisgya.springsecurity.service.userService;

import com.chrisgya.springsecurity.entity.Permission;
import com.chrisgya.springsecurity.entity.Role;
import com.chrisgya.springsecurity.entity.User;
import com.chrisgya.springsecurity.model.AuthenticationResponse;
import com.chrisgya.springsecurity.model.UserDetailsImpl;
import com.chrisgya.springsecurity.model.UserPage;
import com.chrisgya.springsecurity.model.UserParameters;
import com.chrisgya.springsecurity.model.request.*;
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

    Page<User> searchUsers(final String text, UserPage userPage);

    List<User> getUsers();

    void changeEmail(ChangeEmailRequest req);

    void updateUser(UpdateUserRequest req);

    List<Role> getUserRoles(Long id);

    void lockUser(Long id);

    void unLockUser(Long id);

    void enableUser(Long id);

    void disableUser(Long id);

    List<Permission> getUserPermissions(Long id);
}
