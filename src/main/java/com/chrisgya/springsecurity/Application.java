package com.chrisgya.springsecurity;

import com.chrisgya.springsecurity.entity.Permission;
import com.chrisgya.springsecurity.entity.Role;
import com.chrisgya.springsecurity.entity.User;
import com.chrisgya.springsecurity.entity.UserRoles;
import com.chrisgya.springsecurity.repository.PermissionRepository;
import com.chrisgya.springsecurity.repository.RoleRepository;
import com.chrisgya.springsecurity.repository.UserRepository;
import com.chrisgya.springsecurity.repository.UserRolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.transaction.Transactional;
import java.util.*;

@SpringBootApplication
@ConfigurationPropertiesScan
public class Application {

    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRolesRepository userRolesRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {

            if (permissionRepository.count() < 1) {
                InitializeAdminUser();
            }
        };
    }

    @Transactional
    public void InitializeAdminUser() {
        var adminEmail = "admin@chrisgya.com";

        var permissions = new String[]{"can_view_users", "can_lock_user", "can_unlock_user", "can_enable_user",
                "can_disable_user", "can_read_roles", "can_create_role", "can_update_role", "can_delete_role", "can_assign_users_to_role",
                "can_remove_users_from_role", "can_assign_permissions_to_role", "can_remove_permissions_from_role", "can_read_permissions",
                "can_create_permission", "can_update_permission" };

        List<Permission> permissionList = new ArrayList<>();
        Arrays.stream(permissions)
                .forEach(permission -> {
                    var p = new Permission(permission, "");
                    p.setCreatedBy(adminEmail);
                    permissionList.add(p);
                });

        permissionRepository.saveAll(permissionList);

        var roleRequest = new Role("Administrator", "Administrator role");
        roleRequest.setCreatedBy(adminEmail);
        var role = roleRepository.save(roleRequest);

        var userRequest = User.builder()
                .username("admin")
                .email("admin@chrisgya.com")
                .firstName("solomon")
                .lastName("Mensah")
                .enabled(true)
                .confirmed(true)
                .locked(false)
                .password(passwordEncoder.encode("Password@1"))
                .build();

        userRequest.setCreatedBy(adminEmail);
        var user = userRepository.save(userRequest);

        var userRole = new UserRoles(user, role);
        userRole.setCreatedBy(adminEmail);
        userRolesRepository.save(userRole);
    }
}
