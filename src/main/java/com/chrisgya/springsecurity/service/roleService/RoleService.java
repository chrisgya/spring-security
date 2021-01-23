package com.chrisgya.springsecurity.service.roleService;

import com.chrisgya.springsecurity.entity.Role;
import com.chrisgya.springsecurity.model.RolePage;
import com.chrisgya.springsecurity.model.request.CreateRoleRequest;
import com.chrisgya.springsecurity.model.request.UpdateRoleRequest;
import org.springframework.data.domain.Page;

public interface RoleService {
    Page<Role> getRoles(String name, RolePage rolePage);

    Role getRole(Long id);

    Role createRole(CreateRoleRequest req);

    void updateRole(Long id, UpdateRoleRequest req);

    void deleteRole(Long id);
}
