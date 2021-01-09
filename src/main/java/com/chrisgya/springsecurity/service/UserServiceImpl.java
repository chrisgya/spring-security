package com.chrisgya.springsecurity.service;

import com.chrisgya.springsecurity.config.properties.JwtProperties;
import com.chrisgya.springsecurity.config.security.JwtHelper;
import com.chrisgya.springsecurity.controller.AuthController;
import com.chrisgya.springsecurity.entity.RefreshToken;
import com.chrisgya.springsecurity.entity.User;
import com.chrisgya.springsecurity.entity.UserRoles;
import com.chrisgya.springsecurity.entity.UserVerification;
import com.chrisgya.springsecurity.exception.BadRequestException;
import com.chrisgya.springsecurity.exception.NotFoundException;
import com.chrisgya.springsecurity.model.*;
import com.chrisgya.springsecurity.model.request.LoginRequest;
import com.chrisgya.springsecurity.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserVerificationRepository userVerificationRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtHelper jwtHelper;
    private final JwtProperties jwtProperties;
    private final ModelMapper modelMapper;
    private final UserDetailsService userDetailsService;
    private final EmailService emailService;


    @Override
    public AuthenticationResponse login(LoginRequest req) {

        try {
            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            var userDetails = (UserDetailsImpl) authentication.getPrincipal();

            if (!userDetails.isConfirmed()) {
                throw new BadRequestException("please confirm your email");
            }

            if (userDetails.isLocked() && userDetails.getLockExpiryDate().isBefore(Instant.now())) {
                throw new BadRequestException(String.format("account is locked. account would be available on %s", userDetails.getLockExpiryDate()));
            }
            if (!userDetails.isEnabled()) {
                throw new BadRequestException("account is disabled. please contact support team for assistance");
            }

            return AuthenticationResponse.builder()
                    .accessToken(jwtHelper.createJwtForClaims(userDetails))
                    .refreshToken(generateRefreshToken(modelMapper.map(userDetails, User.class)).getToken())
                    .build();

        } catch (AuthenticationException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public AuthenticationResponse refreshToken(String refreshToken) {
        var result = validateRefreshToken(refreshToken);
        var userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(result.getUser().getEmail());

        return AuthenticationResponse.builder()
                .accessToken(jwtHelper.createJwtForClaims(userDetails))
                .refreshToken(generateRefreshToken(modelMapper.map(userDetails, User.class)).getToken())
                .build();
    }


    @Transactional
    @Override
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
                .lastName(req.getLastname())
                .password(passwordEncoder.encode(req.getPassword()))
                .isEnabled(true)
                .build();
        user.setCreatedBy(req.getEmail());


        req.getRoleIds().ifPresent(roleIds -> {
            Set<UserRoles> userRoles = new HashSet<>();
            roleRepository.findAllById(roleIds).stream().collect(Collectors.toSet()).forEach(role -> {
                userRoles.add(new UserRoles(user, role));
            });

            user.setUserRoles(userRoles);
        });


        userRepository.save(user);

        String token = generateVerificationToken(user);
        var confirmationLink = MvcUriComponentsBuilder
                .fromMethodName(AuthController.class, "verifyAccount", token)
                .build().toString();

        sendConfirmationLink(user, confirmationLink);

        return user;
    }

    private void sendConfirmationLink(User user, String url) {
        var subject = "Account confirmation on Chrisgya Site";

        Map<String, Object> thymeLeafProps = new HashMap<>();
        thymeLeafProps.put("name", user.getFirstName());
        thymeLeafProps.put("url", url);

        emailService.sender("account-confirmation-template", subject, user.getEmail(), thymeLeafProps, null, null, null);
    }

    @Transactional
    @Override
    public void verifyAccount(String token) {
        var verificationToken = userVerificationRepository.findByToken(token)
                .orElseThrow(() -> new NotFoundException("invalid token"));

        if (verificationToken.getExpiryDate().isBefore(Instant.now())) {
            throw new BadRequestException("invalid token");
        }

        var user = verificationToken.getUser();
        user.setConfirmed(true);
        userRepository.save(user);
        userVerificationRepository.delete(verificationToken);

    }

    @Override
    public User getCurrentUser() {
        var principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getUsername()));
    }


    @Override
    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("no user found"));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("no user found"));
    }

    @Override
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

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    private String generateVerificationToken(User user) {
        var token = UUID.randomUUID().toString();
        var verificationToken = UserVerification.builder()
                .token(token)
                .user(user)
                .expiryDate(Instant.now().plusSeconds(jwtProperties.getActivationTokenExpirationAfterSeconds()))
                .build();

        userVerificationRepository.save(verificationToken);
        return token;
    }

    private RefreshToken generateRefreshToken(User user) {
        var refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusSeconds(jwtProperties.getRefreshTokenExpiresAfterSeconds()))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    private RefreshToken validateRefreshToken(String token) {
        var refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new BadRequestException("invalid refresh token"));
        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            throw new BadRequestException("refresh token has expired");
        }

        refreshTokenRepository.delete(refreshToken);

        return refreshToken;
    }


}
