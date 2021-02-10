package com.chrisgya.springsecurity.service.roleService;

import com.chrisgya.springsecurity.entity.Permission;
import com.chrisgya.springsecurity.entity.Role;
import com.chrisgya.springsecurity.entity.RolePermissions;
import com.chrisgya.springsecurity.model.RolePage;
import com.chrisgya.springsecurity.model.request.CreateRoleRequest;
import com.chrisgya.springsecurity.model.request.UpdateRoleRequest;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

public interface RoleService {
    Page<Role> getRoles(String name, RolePage rolePage);

    Role getRole(Long id);

    List<Role> getRoles(Set<Long> roleIds);

    List<Permission> getPermissionsByRole(Long roleId);

    Role createRole(CreateRoleRequest req);

    void updateRole(Long id, UpdateRoleRequest req);

    void deleteRole(Long id);

    List<RolePermissions> assignPermissionsToRole(Long roleId, Set<Long> permissionIds);

    void removePermissionsFromRole(Long roleId, Set<Long> permissionIds);
}
