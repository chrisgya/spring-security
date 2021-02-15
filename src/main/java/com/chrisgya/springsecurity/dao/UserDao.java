package com.chrisgya.springsecurity.dao;

import com.chrisgya.springsecurity.entity.Permission;
import com.chrisgya.springsecurity.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
     int save(User user);

     int update(User user) ;

    int delete(int id);

     List<User> findAll();
     List<Permission> findUserPermissionsByUserId(Long userId);
     List<Permission> findUserPermissionsByUserEmail(String userEmail);

    Optional<User> findById(int id);
}
