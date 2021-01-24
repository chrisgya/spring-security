package com.chrisgya.springsecurity.controller;

import com.chrisgya.springsecurity.entity.Role;
import com.chrisgya.springsecurity.entity.RolePermissions;
import com.chrisgya.springsecurity.model.RolePage;
import com.chrisgya.springsecurity.model.request.CreateRoleRequest;
import com.chrisgya.springsecurity.model.request.UpdateRoleRequest;
import com.chrisgya.springsecurity.service.roleService.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/v1/roles")
@Validated
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('can_read_roles','can_create_role','can_update_role','can_delete_role')")
    public Page<Role> getRoles(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String sortDirection,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize
    ) {
        var page = new RolePage();
        page.setSortBy(sortField);
        page.setSortDirection(sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC);
        page.setPageNumber(pageNumber);
        page.setPageSize(pageSize);

        return roleService.getRoles(name, page);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('can_read_permissions','can_create_role','can_update_role','can_delete_role')")
    public Role getRole(@PathVariable Long id) {
        return roleService.getRole(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('can_create_role')")
    public Role createRole(@Valid @RequestBody CreateRoleRequest req) {
        return roleService.createRole(req);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAuthority('can_update_role')")
    public void updateRole(@PathVariable Long id, @Valid @RequestBody UpdateRoleRequest req) {
        roleService.updateRole(id, req);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('can_delete_role')")
    public void deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
    }

    @PutMapping("assign-permissions/{roleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('can_assign_permissions_to_role')")
    public List<RolePermissions> assignPermissionsToRole(@PathVariable Long roleId, @RequestBody Set<Long> permissionIds) {
       return roleService.assignPermissionsToRole(roleId, permissionIds);
    }

    @PutMapping("remove-permissions/{roleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('can_remove_permissions_from_role')")
    public void removePermissionsFromRole(@PathVariable Long roleId, @RequestBody Set<Long> permissionIds) {
        roleService.removePermissionsFromRole(roleId, permissionIds);
    }

}
