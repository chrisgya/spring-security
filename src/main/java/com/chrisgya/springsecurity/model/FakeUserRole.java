//package com.chrisgya.springsecurity.model;
//
//import com.google.common.collect.Sets;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//
//import java.util.Set;
//import java.util.stream.Collectors;
//
//import static com.chrisgya.springsecurity.model.FakeUserPermission.*;
//
//public enum FakeUserRole {
//    STUDENT(Sets.newHashSet()),
//    ADMIN(Sets.newHashSet(COURSE_READ, COURSE_WRITE, STUDENT_READ, STUDENT_WRITE)),
//    ADMINTRAINEE(Sets.newHashSet(COURSE_READ, STUDENT_READ));
//
//    private final Set<FakeUserPermission> permissions;
//
//    FakeUserRole(Set<FakeUserPermission> permissions) {
//        this.permissions = permissions;
//    }
//
//    public Set<FakeUserPermission> getPermissions() {
//        return permissions;
//    }
//
//    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
//        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
//                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
//                .collect(Collectors.toSet());
//        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
//        return permissions;
//    }
//}
