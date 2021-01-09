package com.chrisgya.springsecurity.repository;

import com.chrisgya.springsecurity.entity.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRolesRepository extends JpaRepository<UserRoles, Long> {
}
