package com.chrisgya.springsecurity.controller;

import com.chrisgya.springsecurity.entity.Permission;
import com.chrisgya.springsecurity.entity.Role;
import com.chrisgya.springsecurity.entity.User;
import com.chrisgya.springsecurity.model.UserPage;
import com.chrisgya.springsecurity.model.UserParameters;
import com.chrisgya.springsecurity.service.userService.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/acountmgt")
@Validated
@RequiredArgsConstructor
public class AccountMgtController {
    private final UserService userService;

    //    hasRole('ROLE_') hasAnyRole('ROLE_') hasAuthority('permission') hasAnyAuthority('permission')

    @GetMapping("users")
    @PreAuthorize("hasAuthority('can_read_users')")
    public Page<User> getUsers(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Boolean locked,
            @RequestParam(required = false) Boolean enabled,
            @RequestParam(required = false) Boolean confirmed,
            @RequestParam(required = false) String sortDirection,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize
    ) {
        var params = UserParameters.builder()
                .usersFirstName(firstName)
                .usersLastName(lastName)
                .usersUsername(username)
                .usersEmail(email)
                .usersLocked(locked)
                .usersEnabled(enabled)
                .usersConfirmed(confirmed)
                .build();

        var userPage = new UserPage();
        userPage.setSortBy(sortField);
        userPage.setSortDirection(sortDirection.equalsIgnoreCase("asc")? Sort.Direction.ASC : Sort.Direction.DESC);
        userPage.setPageNumber(pageNumber);
        userPage.setPageSize(pageSize);

       return userService.getUsers(params, userPage);
    }

    @GetMapping("search-users")
    @PreAuthorize("hasAuthority('can_read_users')")
    public Page<User> searchUsers(
            @RequestParam String searchText,
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize
    ) {
        var userPage = new UserPage();
        userPage.setPageNumber(pageNumber);
        userPage.setPageSize(pageSize);

        return userService.searchUsers(searchText, userPage);
    }

    @GetMapping("users/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('can_read_users','can_lock_user','can_unlock_user','can_enable_user','can_disable_user','can_delete_user')")
    public User getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @GetMapping("users/{id}/roles")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('can_read_users','can_lock_user','can_unlock_user','can_enable_user','can_disable_user','can_delete_user')")
    public List<Role> getUserRoles(@PathVariable Long id) {
        return userService.getUserRoles(id);
    }

    @GetMapping("users/{id}/permissions")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('can_read_users','can_lock_user','can_unlock_user','can_enable_user','can_disable_user','can_delete_user')")
    public List<Permission> getUserPermissions(@PathVariable Long id) {
        return userService.getUserPermissions(id);
    }

    @PutMapping("users/{id}/lock")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('can_lock_user')")
    public void lockUser(@PathVariable Long id) {
         userService.lockUser(id);
    }

    @PutMapping("users/{id}/unlock")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('can_unlock_user')")
    public void unLockUser(@PathVariable Long id) {
         userService.unLockUser(id);
    }

    @PutMapping("users/{id}/enable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('can_enable_user')")
    public void enableUser(@PathVariable Long id) {
         userService.enableUser(id);
    }

    @PutMapping("users/{id}/disable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('can_unlock_user')")
    public void disableUser(@PathVariable Long id) {
         userService.disableUser(id);
    }

}
