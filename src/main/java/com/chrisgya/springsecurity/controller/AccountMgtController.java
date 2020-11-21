package com.chrisgya.springsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/acountmgt")
@Validated
@RequiredArgsConstructor
public class AccountMgtController {
    //    hasRole('ROLE_') hasAnyRole('ROLE_') hasAuthority('permission') hasAnyAuthority('permission')

    //view users
    //view user details
    //view user's roles
    //lock account
    //unlock account
    //disable account
    //enable account
    //delete account

    @GetMapping("users")
    @PreAuthorize("hasAuthority('can_read_users')")
    public ResponseEntity getUsers(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String LastName,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Boolean isLocked,
            @RequestParam(required = false) Boolean isEnabled,
            @RequestParam(required = false) Boolean isConfirmed,
            @RequestParam(required = false) Boolean isDeleted,
            @RequestParam(required = false) String sortDirection,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize
    ) {
        //with pagination and sorting
        return ResponseEntity.ok(null);
    }

    @GetMapping("users/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('can_read_users','can_lock_user','can_unlock_user','can_enable_user','can_disable_user','can_delete_user')")
    public void getUser(@PathVariable String id) {

    }

    @GetMapping("users/{id}/roles")
    @PreAuthorize("hasAnyAuthority('can_read_users','can_lock_user','can_unlock_user','can_enable_user','can_disable_user','can_delete_user')")
    public ResponseEntity getUserRoles(@PathVariable String id) {
        return ResponseEntity.ok(null);
    }

    @PutMapping("lock")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('can_lock_user')")
    public void lockAccount(@PathVariable String id) {

    }

    @PutMapping("unlock")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('can_unlock_user')")
    public void unLockAccount(@PathVariable String id) {

    }

    @PutMapping("enable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('can_enable_user')")
    public void enableAccount(@PathVariable String id) {

    }

    @PutMapping("disable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('can_unlock_user')")
    public void disableAccount(@PathVariable String id) {

    }

}
