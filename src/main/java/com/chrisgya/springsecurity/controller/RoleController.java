package com.chrisgya.springsecurity.controller;

import com.chrisgya.springsecurity.entity.Permission;
import com.chrisgya.springsecurity.entity.Role;
import com.chrisgya.springsecurity.entity.RolePermissions;
import com.chrisgya.springsecurity.model.RolePage;
import com.chrisgya.springsecurity.model.request.CreateRoleRequest;
import com.chrisgya.springsecurity.model.request.UpdateRoleRequest;
import com.chrisgya.springsecurity.service.roleService.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Tag(name = "Role management By Administrator", description = "Role management - view, create, update and delete")
@SecurityRequirement(name = "api")
@RestController
@RequestMapping("api/v1/roles")
@Validated
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @Operation(summary = "get roles", description = "get all roles or search role by name. you can optional sort and paginate the result. you must have one of these permissions in your role to be able to access this resource: (can_read_roles,can_create_role,can_update_role,can_delete_role)")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('can_read_roles','can_create_role','can_update_role','can_delete_role')")
    public Page<Role> getRoles(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String sortDirection,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize
    ) {
        var page = new RolePage();
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

        return roleService.getRoles(name, page);
    }

    @Operation(summary = "get a role by ID", description = "you must have one of these permissions in your role to be able to access this resource: (can_read_permissions,can_create_role,can_update_role,can_delete_role)")
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('can_read_permissions','can_create_role','can_update_role','can_delete_role')")
    public Role getRole(@PathVariable Long id) {
        return roleService.getRole(id);
    }

    @Operation(summary = "get role's permissions by Role ID", description = "you must have one of these permissions in your role to be able to access this resource: (can_read_permissions,can_create_role,can_update_role,can_delete_role)")
    @GetMapping("{id}/permissions")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('can_read_permissions','can_create_role','can_update_role','can_delete_role')")
    public List<Permission> getPermissionsByRole(@PathVariable Long id) {
        return roleService.getPermissionsByRole(id);
    }

    @Operation(summary = "create a new role", description = "a new role must include permission(s) .you must have one of these permissions in your role to be able to access this resource: (can_create_role)")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('can_create_role')")
    public Role createRole(@Valid @RequestBody CreateRoleRequest createRoleRequest) {
        return roleService.createRole(createRoleRequest);
    }

    @Operation(summary = "update a role details", description = "you must have one of these permissions in your role to be able to access this resource: (can_update_role)")
    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('can_update_role')")
    public void updateRole(@PathVariable Long id, @Valid @RequestBody UpdateRoleRequest req) {
        roleService.updateRole(id, req);
    }

    @Operation(summary = "delete a role", description = "you must have one of these permissions in your role to be able to access this resource: (can_delete_role)")
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('can_delete_role')")
    public void deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
    }

    @Operation(summary = "add permission(s) to a role", description = "you must have one of these permissions in your role to be able to access this resource: (can_assign_permissions_to_role)")
    @PutMapping("assign-permissions/{roleId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('can_assign_permissions_to_role')")
    public List<RolePermissions> assignPermissionsToRole(@PathVariable Long roleId, @RequestBody Set<Long> permissionIds) {
        return roleService.assignPermissionsToRole(roleId, permissionIds);
    }

    @Operation(summary = "remove permission(s) from a role", description = "you must have one of these permissions in your role to be able to access this resource: (can_remove_permissions_from_role)")
    @PutMapping("remove-permissions/{roleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('can_remove_permissions_from_role')")
    public void removePermissionsFromRole(@PathVariable Long roleId, @RequestBody Set<Long> permissionIds) {
        roleService.removePermissionsFromRole(roleId, permissionIds);
    }

}
