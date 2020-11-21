package com.chrisgya.springsecurity.repository;

import com.chrisgya.springsecurity.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {
//    List<Course> findByTitleContaining(String title);
//    List<Course> findByFeeLessThan(double fee);
}
