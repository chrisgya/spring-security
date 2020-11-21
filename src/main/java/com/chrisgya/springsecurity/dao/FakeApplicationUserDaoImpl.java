//package com.chrisgya.springsecurity.dao;
//
//import com.chrisgya.springsecurity.model.ApplicationUser;
//import com.google.common.collect.Lists;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//
//import static com.chrisgya.springsecurity.model.FakeUserRole.*;
//
//@RequiredArgsConstructor
//@Repository("fake")
//public class FakeApplicationUserDaoImpl  implements ApplicationUserDao {
//    private final PasswordEncoder passwordEncoder;
//
//    @Override
//    public Optional<ApplicationUser> getApplicationUserByUsername(String username) {
//        return getApplicationUsers().stream()
//                .filter(u -> u.getUsername().equalsIgnoreCase(username))
//                .findFirst();
//    }
//
//    private List<ApplicationUser> getApplicationUsers() {
//        List<ApplicationUser> applicationUsers = Lists.newArrayList(
//                new ApplicationUser(
//                        "annasmith",
//                        passwordEncoder.encode("password"),
//                        STUDENT.getGrantedAuthorities(),
//                        true,
//                        true,
//                        true,
//                        true
//                ),
//                new ApplicationUser(
//                        "linda",
//                        passwordEncoder.encode("password"),
//                        ADMIN.getGrantedAuthorities(),
//                        true,
//                        true,
//                        true,
//                        true
//                ),
//                new ApplicationUser(
//                        "tom",
//                        passwordEncoder.encode("password"),
//                        ADMINTRAINEE.getGrantedAuthorities(),
//                        true,
//                        true,
//                        true,
//                        true
//                )
//        );
//
//        return applicationUsers;
//    }
//
//}
