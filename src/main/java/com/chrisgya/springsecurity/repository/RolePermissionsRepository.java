package com.chrisgya.springsecurity.repository;

import com.chrisgya.springsecurity.entity.RolePermissions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RolePermissionsRepository extends JpaRepository<RolePermissions, Long> {
    List<RolePermissions> findRolePermissionsByRoleId(Long roleId);
}
