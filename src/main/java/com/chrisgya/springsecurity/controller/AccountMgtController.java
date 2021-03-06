package com.chrisgya.springsecurity.controller;

import com.chrisgya.springsecurity.entity.Permission;
import com.chrisgya.springsecurity.entity.Role;
import com.chrisgya.springsecurity.entity.User;
import com.chrisgya.springsecurity.entity.UserRoles;
import com.chrisgya.springsecurity.model.UserPage;
import com.chrisgya.springsecurity.model.UserParameters;
import com.chrisgya.springsecurity.service.userService.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Tag(name = "User management By Administrator", description = "System administrator's user management")
@SecurityRequirement(name = "api")
@RestController
@RequestMapping("api/v1/acountmgt")
@RequiredArgsConstructor
public class AccountMgtController {
    private final UserService userService;

    //    hasRole('ROLE_') hasAnyRole('ROLE_') hasAuthority('permission') hasAnyAuthority('permission')

    @Operation(summary = "get users with optional filters", description = "you must have one of these permissions in your role to be able to access this resource: (can_read_users)")
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
        if (StringUtils.hasText(sortField))
            userPage.setSortBy(sortField);
        if (StringUtils.hasText(sortDirection))
            userPage.setSortDirection(sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC);
        if (pageNumber != null)
            userPage.setPageNumber(pageNumber);
        if (pageSize != null)
            userPage.setPageSize(pageSize);

        return userService.getUsers(params, userPage);
    }

    @Operation(summary = "search users", description = "this uses postgres full-text search feature.you must have one of these permissions in your role to be able to access this resource: (can_read_users)")
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

    @Operation(summary = "get user by user ID", description = "you must have one of these permissions in your role to be able to access this resource: (can_read_users,can_lock_user,can_unlock_user,can_enable_user,can_disable_user,can_delete_user)")
    @GetMapping("users/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('can_read_users','can_lock_user','can_unlock_user','can_enable_user','can_disable_user','can_delete_user')")
    public User getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @Operation(summary = "get roles for provided user ID", description = "you must have one of these permissions in your role to be able to access this resource: (can_read_users,can_lock_user,can_unlock_user,can_enable_user,can_disable_user,can_delete_user)")
    @GetMapping("users/{id}/roles")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('can_read_users','can_lock_user','can_unlock_user','can_enable_user','can_disable_user','can_delete_user')")
    public List<Role> getUserRoles(@PathVariable Long id) {
        return userService.getUserRoles(id);
    }

    @Operation(summary = "get permissions for provided user ID", description = "you must have one of these permissions in your role to be able to access this resource: (can_read_users,can_lock_user,can_unlock_user,can_enable_user,can_disable_user,can_delete_user)")
    @GetMapping("users/{id}/permissions")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('can_read_users','can_lock_user','can_unlock_user','can_enable_user','can_disable_user','can_delete_user')")
    public List<Permission> getUserPermissions(@PathVariable Long id) {
        return userService.getUserPermissions(id);
    }

    @Operation(summary = "assign user(s) to a role", description = "you must have one of these permissions in your role to be able to access this resource: (can_assign_users_to_role)")
    @PutMapping("assign-users-to-role/{roleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('can_assign_users_to_role')")
    public List<UserRoles> assignUsersToRole(@PathVariable Long roleId, @RequestBody Set<Long> userIds) {
        return userService.assignUsersToRole(roleId, userIds);
    }

    @Operation(summary = "remove user(s) from a role", description = "you must have one of these permissions in your role to be able to access this resource: (can_remove_users_from_role)")
    @PutMapping("remove-users-from-role/{roleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('can_remove_users_from_role')")
    public void removeUsersFromRole(@PathVariable Long roleId, @RequestBody Set<Long> userIds) {
        userService.removeUsersFromRole(roleId, userIds);
    }

    @Operation(summary = "lock provided user by user ID", description = "you must have one of these permissions in your role to be able to access this resource: (can_lock_user)")
    @PutMapping("users/{id}/lock")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('can_lock_user')")
    public void lockUser(@PathVariable Long id) {
        userService.lockUser(id);
    }

    @Operation(summary = "unlock provided user by user ID", description = "you must have one of these permissions in your role to be able to access this resource: (can_unlock_user)")
    @PutMapping("users/{id}/unlock")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('can_unlock_user')")
    public void unLockUser(@PathVariable Long id) {
        userService.unLockUser(id);
    }

    @Operation(summary = "enable provided user by user ID", description = "you must have one of these permissions in your role to be able to access this resource: (can_enable_user)")
    @PutMapping("users/{id}/enable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('can_enable_user')")
    public void enableUser(@PathVariable Long id) {
        userService.enableUser(id);
    }

    @Operation(summary = "disable provided user by user ID", description = "you must have one of these permissions in your role to be able to access this resource: (can_unlock_user)")
    @PutMapping("users/{id}/disable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('can_unlock_user')")
    public void disableUser(@PathVariable Long id) {
        userService.disableUser(id);
    }

}
