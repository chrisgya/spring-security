package com.chrisgya.springsecurity.controller;

import com.chrisgya.springsecurity.entity.Permission;
import com.chrisgya.springsecurity.model.PermissionPage;
import com.chrisgya.springsecurity.model.request.CreatePermissionRequest;
import com.chrisgya.springsecurity.model.request.UpdatePermissionRequest;
import com.chrisgya.springsecurity.service.permissionService.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/permissions")
@Validated
@RequiredArgsConstructor
public class PermissionController {
    private final PermissionService permissionService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('can_read_permissions', 'can_create_permission', 'can_update_permission', 'can_delete_permission')")
    public Page<Permission> getPermissions(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String sortDirection,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize
    ) {
        var page = new PermissionPage();
        page.setSortBy(sortField);
        page.setSortDirection(sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC);
        page.setPageNumber(pageNumber);
        page.setPageSize(pageSize);

        return permissionService.getPermissions(name, page);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('can_read_permissions', 'can_create_permission', 'can_update_permission', 'can_delete_permission')")
    public Permission getPermission(@PathVariable Long id) {
        return permissionService.getPermission(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('can_create_permission')")
    public Permission createPermission(@Valid @RequestBody CreatePermissionRequest req) {
        return permissionService.createPermission(req);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAuthority('can_update_permission')")
    public void updatePermission(@PathVariable Long id, @Valid @RequestBody UpdatePermissionRequest req) {
        permissionService.updatePermission(id, req);
    }
}
