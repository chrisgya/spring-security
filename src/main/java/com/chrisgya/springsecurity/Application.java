package com.chrisgya.springsecurity;

import com.chrisgya.springsecurity.entity.*;
import com.chrisgya.springsecurity.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.transaction.Transactional;
import java.util.*;

@SpringBootApplication
@ConfigurationPropertiesScan
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Transactional
    @Bean
    public CommandLineRunner commandLineRunner(UserRolesRepository userRolesRepository, UserRepository userRepository, RoleRepository roleRepository, RolePermissionsRepository rolePermissionsRepository, PermissionRepository permissionRepository, PasswordEncoder passwordEncoder) {
        return args -> {

            if (permissionRepository.count() < 1) {
                var adminEmail = "admin@chrisgya.com";

                var permissions = new String[]{"can_read_users", "can_lock_user", "can_unlock_user", "can_enable_user",
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

                var permissionsResult = permissionRepository.saveAll(permissionList);

                var roleRequest = Role.builder()
                        .name("Administrator")
                        .description("Administrator role")
                        .createdBy(adminEmail)
                        .build();

                var roleResult = roleRepository.save(roleRequest);

                Set<RolePermissions> rolePermissionsSet = new HashSet<>();
                permissionsResult.stream().forEach(permission -> {
                    var rp = new RolePermissions(permission, roleResult);
                    rp.setCreatedBy(adminEmail);
                    rolePermissionsSet.add(rp);
                });

                rolePermissionsRepository.saveAll(rolePermissionsSet);

                var userRequest = User.builder()
                        .username("admin")
                        .email("admin@chrisgya.com")
                        .firstName("solomon")
                        .lastName("Mensah")
                        .mobileNo("08081535330")
                        .enabled(true)
                        .confirmed(true)
                        .locked(false)
                        .password(passwordEncoder.encode("Password@1"))
                        .build();

                userRequest.setCreatedBy(adminEmail);
                var user = userRepository.save(userRequest);

                var userRole = new UserRoles(user, roleResult);
                userRole.setCreatedBy(adminEmail);
                userRolesRepository.save(userRole);
            }
        };
    }

}
