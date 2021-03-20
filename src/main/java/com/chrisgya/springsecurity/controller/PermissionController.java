package com.chrisgya.springsecurity.controller;

import com.chrisgya.springsecurity.entity.Permission;
import com.chrisgya.springsecurity.entity.Role;
import com.chrisgya.springsecurity.model.PermissionPage;
import com.chrisgya.springsecurity.model.request.CreatePermissionRequest;
import com.chrisgya.springsecurity.model.request.UpdatePermissionRequest;
import com.chrisgya.springsecurity.service.permissionService.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "Permission management By Administrator", description = "Permission management - view, create, update and delete")
@SecurityRequirement(name = "api")
@RestController
@RequestMapping("api/v1/permissions")
@Validated
@RequiredArgsConstructor
public class PermissionController {
    private final PermissionService permissionService;

    @Operation(summary = "get permissions or search permission by name", description = "you can optionally sort and paginate the result .you must have one of these permissions in your role to be able to access this resource: (can_read_permissions, can_create_permission, can_update_permission, can_delete_permission)")
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
        if (StringUtils.hasText(sortDirection)) {
            page.setSortDirection(sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC);
        }
        if (StringUtils.hasText(sortField)) {
            page.setSortBy(sortField);
        }
        if (pageNumber != null) {
            page.setPageNumber(pageNumber);
        }
        if (pageSize != null) {
            page.setPageSize(pageSize);
        }

        return permissionService.getPermissions(name, page);
    }

    @Operation(summary = "get a permission by ID", description = "you must have one of these permissions in your role to be able to access this resource: (can_read_permissions, can_create_permission, can_update_permission, can_delete_permission)")
    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('can_read_permissions', 'can_create_permission', 'can_update_permission', 'can_delete_permission')")
    public Permission getPermission(@PathVariable Long id) {
        return permissionService.getPermission(id);
    }

    @Operation(summary = "get all roles using provided permission ID", description = "you must have one of these permissions in your role to be able to access this resource: (can_read_permissions, can_create_permission, can_update_permission, can_delete_permission)")
    @GetMapping("{id}/roles")
    @PreAuthorize("hasAnyAuthority('can_read_permissions', 'can_create_permission', 'can_update_permission', 'can_delete_permission')")
    public List<Role> getPermissionRoles(@PathVariable Long id) {
        return permissionService.getPermissionRoles(id);
    }

    @Operation(summary = "create new permission", description = "you must have one of these permissions in your role to be able to access this resource: (can_create_permission)")
    @PostMapping
    @PreAuthorize("hasAuthority('can_create_permission')")
    public Permission createPermission(@Valid @RequestBody CreatePermissionRequest req) {
        return permissionService.createPermission(req);
    }

    @Operation(summary = "update permission details", description = "you must have one of these permissions in your role to be able to access this resource: (can_update_permission)")
    @PutMapping("{id}")
    @PreAuthorize("hasAuthority('can_update_permission')")
    public void updatePermission(@PathVariable Long id, @Valid @RequestBody UpdatePermissionRequest req) {
        permissionService.updatePermission(id, req);
    }
}
