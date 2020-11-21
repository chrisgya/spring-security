package com.chrisgya.springsecurity.service;

import com.chrisgya.springsecurity.entity.Role;
import com.chrisgya.springsecurity.model.RolePage;
import com.chrisgya.springsecurity.model.UserSpecification;
import com.chrisgya.springsecurity.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RoleServiceImpl {
    private final RoleRepository roleRepository;

    public Page<Role> getRoles(String name, RolePage rolePage){

        Specification spec = Specification.where(UserSpecification.userUsernameEquals(name));
        Sort sort = Sort.by(rolePage.getSortDirection(), rolePage.getSortBy());
        Pageable pageable = PageRequest.of(rolePage.getPageNumber(),rolePage.getPageSize(), sort);
        return roleRepository.findAll(spec, pageable);
    }

}
