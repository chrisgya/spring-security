package com.chrisgya.springsecurity.service.userService;

import com.chrisgya.springsecurity.config.properties.FrontEndUrlProperties;
import com.chrisgya.springsecurity.config.properties.JwtProperties;
import com.chrisgya.springsecurity.config.properties.MailSubjectProperties;
import com.chrisgya.springsecurity.config.properties.MailTemplateProperties;
import com.chrisgya.springsecurity.config.security.TokenCreator;
import com.chrisgya.springsecurity.dao.UserDao;
import com.chrisgya.springsecurity.entity.*;
import com.chrisgya.springsecurity.exception.BadRequestException;
import com.chrisgya.springsecurity.exception.NotFoundException;
import com.chrisgya.springsecurity.model.*;
import com.chrisgya.springsecurity.model.querySpecs.UserSpecification;
import com.chrisgya.springsecurity.model.request.*;
import com.chrisgya.springsecurity.repository.*;
import com.chrisgya.springsecurity.service.emailService.EmailService;
import com.chrisgya.springsecurity.service.fileStorage.FileStorageService;
import com.chrisgya.springsecurity.service.roleService.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static com.chrisgya.springsecurity.utils.validations.ValidationMessage.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserVerificationRepository userVerificationRepository;
    private final ForgottenPasswordRepository forgottenPasswordRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenCreator tokenCreator;
    private final JwtProperties jwtProperties;
    private final ModelMapper modelMapper;
    private final UserDetailsService userDetailsService;
    private final EmailService emailService;
    private final MailTemplateProperties mailTemplateProperties;
    private final MailSubjectProperties mailSubjectProperties;
    private final FrontEndUrlProperties frontEndUrlProperties;
    private final FileStorageService fileStorageService;
    private final UserRolesRepository userRolesRepository;
    private final UserDao userDao;

    @Override
    public AuthenticationResponse login(LoginRequest req) {
        log.info("**************************************    BEGIN   *********************************************");
        log.info("LoginRequest: {}", req);
        log.info("**************************************    BEGIN   *********************************************");

        try {
            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            var userDetails = (UserDetailsImpl) authentication.getPrincipal();

            if (!userDetails.isConfirmed()) {
                throw new BadRequestException(CONFIRM_EMAIL);
            }

            if (userDetails.isLocked() && userDetails.getLockExpiredAt().isBefore(Instant.now())) {
                throw new BadRequestException(String.format(ACCOUNT_LOCKED, userDetails.getLockExpiredAt()));
            }
            if (!userDetails.isEnabled()) {
                throw new BadRequestException(ACCOUNT_DISABLED);
            }

            return AuthenticationResponse.builder()
                    .accessToken(tokenCreator.createJwtForClaims(userDetails))
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
                .accessToken(tokenCreator.createJwtForClaims(userDetails))
                .refreshToken(generateRefreshToken(modelMapper.map(userDetails, User.class)).getToken())
                .build();
    }

    @Override
    public boolean checkIfUsernameExist(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean checkIfEmailExist(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    @Override
    public User registerUser(RegisterUserRequest req) {

        var userRequest = User.builder()
                .username(req.getUsername())
                .email(req.getEmail())
                .firstName(req.getFirstName())
                .middleName(req.getMiddleName())
                .lastName(req.getLastName())
                .password(passwordEncoder.encode(req.getPassword()))
                .enabled(true)
                .build();
        userRequest.setCreatedBy(req.getEmail());
        var user = userRepository.save(userRequest);


        req.getRoleIds().ifPresent(roleIds -> {
            Set<UserRoles> userRoles = new HashSet<>();
            roleService.getRoles(roleIds).stream().collect(Collectors.toSet()).forEach(role -> {
                var userRole = new UserRoles(user, role);
                userRole.setCreatedBy(req.getEmail());
                userRoles.add(userRole);
            });

            userRolesRepository.saveAll(userRoles);
        });


        String token = generateVerificationToken(user);

        //NB: change server-side link to front-end link
//        var confirmationLink = MvcUriComponentsBuilder
//                .fromMethodName(AuthController.class, "verifyAccount", token)
//                .build().toString();

        //front-end link
        var confirmationLink = frontEndUrlProperties.getConfirmAccount() + token;

        sendConfirmationLink(user, confirmationLink);

        return user;
    }

    @Override
    public void requestConfirmationLink(String email) {

        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND, "user")));

        var token = generateVerificationToken(user);

        var confirmationLink = frontEndUrlProperties.getConfirmAccount() + token;

        sendConfirmationLink(user, confirmationLink);
    }

    private void sendConfirmationLink(User user, String url) {
        Map<String, Object> thymeLeafProps = new HashMap<>();
        thymeLeafProps.put("name", user.getFirstName());
        thymeLeafProps.put("url", url);

        emailService.sender(mailTemplateProperties.getConfirmAccount(), mailSubjectProperties.getConfirmAccount(), user.getEmail(), thymeLeafProps, null, null, null);
    }


    @Transactional
    @Override
    public void verifyAccount(String token) {
        var verificationToken = userVerificationRepository.findByToken(token)
                .orElseThrow(() -> new NotFoundException("invalid token"));

        if (verificationToken.getExpiredAt().isBefore(Instant.now())) {
            throw new BadRequestException("invalid token");
        }

        var user = verificationToken.getUser();
        user.setConfirmed(true);
        userRepository.save(user);
        userVerificationRepository.delete(verificationToken);

    }

    @Override
    public void forgottenPassword(String email) {
        var user = userRepository.findByEmail(email);
        if (!user.isPresent()) {
            return;
        }
        var forgottenPassword = new ForgottenPassword(user.get(), UUID.randomUUID().toString(), Instant.now().plusSeconds(jwtProperties.getActivationTokenExpirationAfterSeconds()));
        forgottenPasswordRepository.save(forgottenPassword);

        sendResetPasswordLink(user.get(), frontEndUrlProperties.getResetPassword() + forgottenPassword.getToken());
    }

    private void sendResetPasswordLink(User user, String url) {
        Map<String, Object> thymeLeafProps = new HashMap<>();
        thymeLeafProps.put("name", user.getFirstName());
        thymeLeafProps.put("url", url);

        emailService.sender(mailTemplateProperties.getResetPassword(), mailSubjectProperties.getResetPassword(), user.getEmail(), thymeLeafProps, null, null, null);
    }

    @Transactional
    @Override
    public void resetPassword(String token, ResetPasswordRequest req) {
        var forgottenPassword = forgottenPasswordRepository.findByToken(token)
                .orElseThrow(() -> new BadRequestException(INVALID_TOKEN));

        if (forgottenPassword.getExpiredAt().isBefore(Instant.now())) {
            new BadRequestException(EXPIRED_TOKEN);
        }

        var user = userRepository.findById(forgottenPassword.getUser().getId())
                .orElseThrow(() -> new BadRequestException(INVALID_USER_TOKEN));

        user.setPassword(passwordEncoder.encode(req.getPassword()));
        userRepository.save(user);

        forgottenPasswordRepository.delete(forgottenPassword);
    }

    @Override
    public void changePassword(ChangePasswordRequest req) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        var user = userRepository.findByEmail(authentication.getPrincipal().toString())
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND, "user")));

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), req.getPassword()));

        user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        userRepository.save(user);
    }


    @Override
    public UserDetailsImpl getCurrentUser() {
        return (UserDetailsImpl) userDetailsService.loadUserByUsername(getCurrentUserEmail());
    }

    @Override
    public Set<Role> getCurrentUserRoles(){
       return userRolesRepository.findUserRolesByUserEmail(getCurrentUserEmail())
                .stream().map(userRoles -> userRoles.getRole()).collect(Collectors.toSet());
    }

    @Override
    public List<Permission> getCurrentUserPermissions() {
        return userDao.findUserPermissionsByUserEmail(getCurrentUserEmail());
    }


    private String getCurrentUserEmail() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getPrincipal().toString();
    }

    @Override
    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND, "user")));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND, "user")));
    }

    @Override
    public Page<User> getUsers(UserParameters params, UserPage userPage) {
        Specification usernameSpec = UserSpecification.userUsernameEquals(params.getUsersUsername());
        Specification emailSpec = UserSpecification.userEmailEquals(params.getUsersEmail());
        Specification firstNameSpec = UserSpecification.userFirstnameEquals(params.getUsersFirstName());
        Specification lastNameSpec = UserSpecification.userLastnameEquals(params.getUsersLastName());
        Specification isLockedSpec = UserSpecification.userIsLockedEquals(params.getUsersLocked());
        Specification isEnabledSpec = UserSpecification.userIsEnabledEquals(params.getUsersEnabled());
        Specification isConfirmedSpec = UserSpecification.userIsConfirmedEquals(params.getUsersConfirmed());

        Specification spec = Specification.where(usernameSpec)
                .and(emailSpec)
                .and(firstNameSpec)
                .and(lastNameSpec)
                .and(isLockedSpec)
                .and(isEnabledSpec)
                .and(isConfirmedSpec);

        Sort sort = Sort.by(userPage.getSortDirection(), userPage.getSortBy());
        Pageable pageable = PageRequest.of(userPage.getPageNumber(), userPage.getPageSize(), sort);
        return userRepository.findAll(spec, pageable);
    }

    @Override
    public Page<User> searchUsers(String text, UserPage userPage) {
        Pageable pageable = PageRequest.of(userPage.getPageNumber(), userPage.getPageSize());
        var searchQuery = text.trim().replaceAll(" ", "|");
        return userRepository.searchUsers(searchQuery, pageable);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public void changeEmail(ChangeEmailRequest req) {
        if (checkIfEmailExist(req.getEmail())) {
            throw new BadRequestException(String.format(ALREADY_TAKEN, "email"));
        }


        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var user = getUserByEmail(authentication.getPrincipal().toString());
        user.setEmail(req.getEmail());
        user.setConfirmed(false);
        userRepository.save(user);

        var token = generateVerificationToken(user);
        var confirmationLink = frontEndUrlProperties.getConfirmAccount() + token;
        sendConfirmationLink(user, confirmationLink);
    }

    @Override
    public User updateUser(UpdateUserRequest req) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var user = getUserByEmail(authentication.getPrincipal().toString());

        user.setFirstName(req.getFirstName());
        user.setMiddleName(req.getMiddleName());
        user.setLastName(req.getLastName());

        return userRepository.save(user);
    }

    @Override
    public User updateUserPicture(MultipartFile picture) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var user = getUserByEmail(authentication.getPrincipal().toString());

        var storedFileResponse = fileStorageService.storeFile(picture, true, null);
        user.setPictureUrl(storedFileResponse.getPath());
        log.info("storedFileResponse: {}", storedFileResponse);

        return userRepository.save(user);
    }


    @Override
    public List<Role> getUserRoles(Long id) {
        return userRolesRepository.findUserRolesByUserId(id)
                .stream().map(userRoles -> userRoles.getRole())
                .collect(Collectors.toList());
    }

    @Override
    public void lockUser(Long id) {
        var user = getUser(id);
        if (user.isLocked()) {
            throw new BadRequestException("user already locked");
        }
        user.setLocked(true);
        userRepository.save(user);
    }

    @Override
    public void unLockUser(Long id) {
        var user = getUser(id);
        if (!user.isLocked()) {
            throw new BadRequestException("user is not locked");
        }
        user.setLocked(false);
        userRepository.save(user);
    }

    @Override
    public void enableUser(Long id) {
        var user = getUser(id);
        if (user.isEnabled()) {
            throw new BadRequestException("user already enabled");
        }
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Override
    public void disableUser(Long id) {
        var user = getUser(id);
        if (!user.isEnabled()) {
            throw new BadRequestException("user is already disabled");
        }
        user.setEnabled(false);
        userRepository.save(user);
    }

    @Override
    public List<Permission> getUserPermissions(Long id) {
        return userDao.findUserPermissionsByUserId(id);
    }

    @Override
    public List<UserRoles> assignUsersToRole(Long roleId, Set<Long> userIds) {
        return userRolesRepository.saveAll(getUserRoles(roleId, userIds));
    }

    @Override
    public void removeUsersFromRole(Long roleId, Set<Long> userIds) {
        userRolesRepository.deleteAll(getUserRoles(roleId, userIds));
    }

    @Override
    public void changeUsername(ChangeUsernameRequest req) {
        if (checkIfUsernameExist(req.getUsername())) {
            throw new BadRequestException(String.format(ALREADY_TAKEN, "username"));
        }

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var user = getUserByEmail(authentication.getPrincipal().toString());
        user.setUsername(req.getUsername());

        userRepository.save(user);
    }

    private Set<UserRoles> getUserRoles(Long roleId, Set<Long> userIds) {
        var role = roleService.getRole(roleId);
        Set<UserRoles> userRoles = new HashSet<>();

        var users = userRepository.findAllById(userIds);
        if (users.isEmpty()) {
            new NotFoundException(String.format(NOT_FOUND, "users"));
        }
        users.stream().forEach(user -> {
            userRoles.add(UserRoles.builder().role(role).user(user).build());
        });

        return userRoles;
    }

    private String generateVerificationToken(User user) {
        var token = UUID.randomUUID().toString();
        var verificationToken = UserVerification.builder()
                .token(token)
                .user(user)
                .expiredAt(Instant.now().plusSeconds(jwtProperties.getActivationTokenExpirationAfterSeconds()))
                .build();

        userVerificationRepository.save(verificationToken);
        return token;
    }

    private RefreshToken generateRefreshToken(User user) {
        var req = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiredAt(Instant.now().plusSeconds(jwtProperties.getRefreshTokenExpiresAfterSeconds()))
                .build();

        var refreshToken = refreshTokenRepository.findByUser(user);
        if (refreshToken.isPresent()) {
            var update = refreshToken.get();
            update.setUser(req.getUser());
            update.setToken(req.getToken());
            update.setExpiredAt(req.getExpiredAt());
            return refreshTokenRepository.save(update);
        }

        return refreshTokenRepository.save(req);
    }

    private RefreshToken validateRefreshToken(String token) {
        var refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new BadRequestException(INVALID_REFRESH_TOKEN));
        if (refreshToken.getExpiredAt().isBefore(Instant.now())) {
            throw new BadRequestException(EXPIRED_REFRESH_TOKEN);
        }

        refreshTokenRepository.delete(refreshToken);

        return refreshToken;
    }


}
