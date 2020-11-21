package com.chrisgya.springsecurity.service;

import com.chrisgya.springsecurity.entity.Permission;
import com.chrisgya.springsecurity.model.PermissionPage;
import com.chrisgya.springsecurity.model.UserSpecification;
import com.chrisgya.springsecurity.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PermissionServiceImpl {
    private final PermissionRepository permissionRepository;

    public Page<Permission> getPermissions(String name, PermissionPage permissionPage) {

        Specification spec = Specification.where(UserSpecification.userUsernameEquals(name));
        Sort sort = Sort.by(permissionPage.getSortDirection(), permissionPage.getSortBy());
        Pageable pageable = PageRequest.of(permissionPage.getPageNumber(), permissionPage.getPageSize(), sort);
        return permissionRepository.findAll(spec, pageable);
    }

}
