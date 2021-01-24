package com.chrisgya.springsecurity.repository;

import com.chrisgya.springsecurity.entity.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRolesRepository extends JpaRepository<UserRoles, Long> {
    List<UserRoles> findUserRolesByUserId(Long userId);
    List<UserRoles> findUserRolesByRoleId(Long roleId);
}
