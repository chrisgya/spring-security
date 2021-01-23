package com.chrisgya.springsecurity.service.permissionService;

import com.chrisgya.springsecurity.entity.Permission;
import com.chrisgya.springsecurity.model.PermissionPage;
import com.chrisgya.springsecurity.model.request.CreatePermissionRequest;
import com.chrisgya.springsecurity.model.request.UpdatePermissionRequest;
import org.springframework.data.domain.Page;

public interface PermissionService {
    Page<Permission> getPermissions(String name, PermissionPage permissionPage);

    Permission getPermission(Long id);

    Permission createPermission(CreatePermissionRequest req);

    void updatePermission(Long id, UpdatePermissionRequest req);

    void deletePermission(Long id);
}
