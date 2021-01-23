package com.chrisgya.springsecurity.service.permissionService;

import com.chrisgya.springsecurity.entity.Permission;
import com.chrisgya.springsecurity.exception.NotFoundException;
import com.chrisgya.springsecurity.model.PermissionPage;
import com.chrisgya.springsecurity.model.UserSpecification;
import com.chrisgya.springsecurity.model.request.CreatePermissionRequest;
import com.chrisgya.springsecurity.model.request.UpdatePermissionRequest;
import com.chrisgya.springsecurity.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import static com.chrisgya.springsecurity.utils.validations.ValidationMessage.NOT_FOUND;

@RequiredArgsConstructor
@Service
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;

    @Override
    public Page<Permission> getPermissions(String name, PermissionPage permissionPage) {

        Specification spec = Specification.where(UserSpecification.userUsernameEquals(name));
        Sort sort = Sort.by(permissionPage.getSortDirection(), permissionPage.getSortBy());
        Pageable pageable = PageRequest.of(permissionPage.getPageNumber(), permissionPage.getPageSize(), sort);
        return permissionRepository.findAll(spec, pageable);
    }

    @Override
    public Permission getPermission(Long id) {
        return permissionRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND, "permission")));
    }

    @Override
    public Permission createPermission(CreatePermissionRequest req) {
        var permission = new Permission(req.getName(), req.getDescription());
        return permissionRepository.save(permission);
    }

    @Override
    public void updatePermission(Long id, UpdatePermissionRequest req) {
        var permission = getPermission(id);
        permission.setName(req.getName());
        permission.setDescription(req.getDescription());
        permissionRepository.save(permission);
    }

    @Override
    public void deletePermission(Long id) {
        var permission = getPermission(id);
        permissionRepository.delete(permission);
    }

}
