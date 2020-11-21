package com.chrisgya.springsecurity.service;

import com.chrisgya.springsecurity.config.security.JwtHelper;
import com.chrisgya.springsecurity.entity.User;
import com.chrisgya.springsecurity.exception.BadRequestException;
import com.chrisgya.springsecurity.exception.NotFoundException;
import com.chrisgya.springsecurity.model.*;
import com.chrisgya.springsecurity.repository.RoleRepository;
import com.chrisgya.springsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtHelper jwtHelper;


    public String login(LoginRequest req) {

        try   {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            var userDetails = (UserDetailsImpl) authentication.getPrincipal();

//        if(!userDetails.isConfirmed()){
//            throw new BadRequestException("please confirm your email");
//        }

            if(userDetails.isLocked() && userDetails.getLockExpiryDate().isBefore(LocalDate.now())){
                throw new BadRequestException(String.format("account is locked. account would be available on %s", userDetails.getLockExpiryDate()));
            }
            if(!userDetails.isEnabled()){
                throw new BadRequestException("account is disabled. please contact support team for assistance");
            }
            if(userDetails.isDeleted()){
                throw new BadRequestException("login failed");
            }

            Map<String, String> claims = new HashMap<>();
            claims.put("username", req.getUsername());

            List<String> authorities = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            claims.put("userId", String.valueOf(userDetails.getId()));
            claims.put("firstName", userDetails.getFirstName());
            claims.put("middleName", userDetails.getMiddleName());
            claims.put("lastname", userDetails.getLastname());

            return jwtHelper.createJwtForClaims(req.getUsername(), claims, authorities);
        } catch (AuthenticationException e) {
            throw new BadRequestException(e.getMessage());
        }
    }


    public User registerUser(RegisterUserRequest req) {

        if (userRepository.existsByUsername(req.getUsername())) {
            throw new BadRequestException("Username is already taken!");
        }

        if (userRepository.existsByEmail(req.getEmail())) {
            throw new BadRequestException("Email is already in use!");
        }

        var user = User.builder()
                .username(req.getUsername())
                .email(req.getEmail())
                .firstName(req.getFirstName())
                .middleName(req.getMiddleName())
                .lastname(req.getLastname())
                .password(passwordEncoder.encode(req.getPassword()))
                .isEnabled(true)
                .build();

        req.getRoleIds().ifPresent(roleIds -> {
            var roles = roleRepository.findAllById(roleIds).stream().collect(Collectors.toSet());
            user.setRoles(roles);
        });

        userRepository.save(user);
        return user;
    }

    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("no user found"));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("no user found"));
    }

    public Page<User> getUsers(UserParameters params, UserPage userPage) {
        Specification usernameSpec = UserSpecification.userUsernameEquals(params.getUsersUsername());
        Specification emailSpec = UserSpecification.userEmailEquals(params.getUsersEmail());
        Specification firstNameSpec = UserSpecification.userFirstnameEquals(params.getUsersFirstName());
        Specification lastNameSpec = UserSpecification.userLastnameEquals(params.getUsersLastName());
        Specification isLockedSpec = UserSpecification.userIsLockedEquals(params.getUsersIsLocked());
        Specification isEnabledSpec = UserSpecification.userIsEnabledEquals(params.getUsersIsEnabled());
        Specification isConfirmedSpec = UserSpecification.userIsConfirmedEquals(params.getUsersIsConfirmed());
        Specification isDeletedSpec = UserSpecification.userIsConfirmedEquals(params.getUsersIsDeleted());

        Specification spec = Specification.where(usernameSpec)
                .or(emailSpec)
                .or(firstNameSpec)
                .or(lastNameSpec)
                .or(isLockedSpec)
                .or(isEnabledSpec)
                .or(isConfirmedSpec)
                .or(isDeletedSpec);

        Sort sort = Sort.by(userPage.getSortDirection(), userPage.getSortBy());
        Pageable pageable = PageRequest.of(userPage.getPageNumber(), userPage.getPageSize(), sort);
        return userRepository.findAll(spec, pageable);
    }

}
