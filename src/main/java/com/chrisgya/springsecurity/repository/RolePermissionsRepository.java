package com.chrisgya.springsecurity.repository;

import com.chrisgya.springsecurity.entity.RolePermissions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolePermissionsRepository extends JpaRepository<RolePermissions, Long> {

}
