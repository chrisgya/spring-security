package com.chrisgya.springsecurity.service.roleService;

import com.chrisgya.springsecurity.entity.Permission;
import com.chrisgya.springsecurity.entity.Role;
import com.chrisgya.springsecurity.entity.RolePermissions;
import com.chrisgya.springsecurity.exception.NotFoundException;
import com.chrisgya.springsecurity.model.RolePage;
import com.chrisgya.springsecurity.model.querySpecs.RoleSpecification;
import com.chrisgya.springsecurity.model.request.CreateRoleRequest;
import com.chrisgya.springsecurity.model.request.UpdateRoleRequest;
import com.chrisgya.springsecurity.repository.RolePermissionsRepository;
import com.chrisgya.springsecurity.repository.RoleRepository;
import com.chrisgya.springsecurity.service.permissionService.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.chrisgya.springsecurity.utils.validations.ValidationMessage.NOT_FOUND;

@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final RolePermissionsRepository rolePermissionsRepository;
    private final PermissionService permissionService;

    @Override
    public Page<Role> getRoles(String name, RolePage rolePage) {
       Specification spec = Specification.where(RoleSpecification.roleNameEquals(name));

        Sort sort = Sort.by(rolePage.getSortDirection(), rolePage.getSortBy());
        Pageable pageable = PageRequest.of(rolePage.getPageNumber(), rolePage.getPageSize(), sort);

        return roleRepository.findAll(spec, pageable);
    }


    @Override
    public Role getRole(Long id) {
        return roleRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND, "role")));
    }

    @Override
    public List<Role> getRoles(Set<Long> roleIds) {
        return roleRepository.findAllById(roleIds);
    }

    @Override
    public List<Permission> getPermissionsByRole(Long roleId) {
        return rolePermissionsRepository.findRolePermissionsByRoleId(roleId)
                .stream().map(rolePermissions -> rolePermissions.getPermission())
                .collect(Collectors.toList());
    }

    @Override
    public Role createRole(CreateRoleRequest req) {
        var role = new Role(req.getName(), req.getDescription());
        return roleRepository.save(role);
    }

    @Override
    public void updateRole(Long id, UpdateRoleRequest req) {
        var role = getRole(id);
        role.setName(req.getName());
        role.setDescription(req.getDescription());
        roleRepository.save(role);
    }

    @Override
    public void deleteRole(Long id) {
        var permission = getRole(id);
        roleRepository.delete(permission);
    }

    @Override
    public List<RolePermissions> assignPermissionsToRole(Long roleId, Set<Long> permissionIds) {
        return rolePermissionsRepository.saveAll(getRolePermissions(roleId, permissionIds));
    }

    @Override
    public void removePermissionsFromRole(Long roleId, Set<Long> permissionIds) {
        rolePermissionsRepository.deleteAll(getRolePermissions(roleId, permissionIds));
    }

    private Set<RolePermissions> getRolePermissions(Long roleId, Set<Long> permissionIds) {
        var role = getRole(roleId);
        Set<RolePermissions> rolePermissions = new HashSet<>();

        var permissions = permissionService.getPermissions(permissionIds);
        if (permissions.isEmpty()) {
            new NotFoundException(String.format(NOT_FOUND, "permissions"));
        }
        permissions.stream().forEach(permission -> {
            rolePermissions.add(RolePermissions.builder().role(role).permission(permission).build());
        });

        return rolePermissions;
    }

}
