package com.chrisgya.springsecurity.service;

import com.chrisgya.springsecurity.config.properties.FrontEndUrlProperties;
import com.chrisgya.springsecurity.config.properties.JwtProperties;
import com.chrisgya.springsecurity.config.properties.MailSubjectProperties;
import com.chrisgya.springsecurity.config.properties.MailTemplateProperties;
import com.chrisgya.springsecurity.config.security.TokenCreator;
import com.chrisgya.springsecurity.entity.*;
import com.chrisgya.springsecurity.exception.BadRequestException;
import com.chrisgya.springsecurity.exception.NotFoundException;
import com.chrisgya.springsecurity.model.*;
import com.chrisgya.springsecurity.model.request.LoginRequest;
import com.chrisgya.springsecurity.model.request.RegisterUserRequest;
import com.chrisgya.springsecurity.model.request.ResetPasswordRequest;
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

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static com.chrisgya.springsecurity.utils.validations.ValidationMessage.*;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
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


    @Override
    public AuthenticationResponse login(LoginRequest req) {

        try {
            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            var userDetails = (UserDetailsImpl) authentication.getPrincipal();

            if (!userDetails.isConfirmed()) {
                throw new BadRequestException(CONFIRM_EMAIL);
            }

            if (userDetails.isLocked() && userDetails.getLockExpiryDate().isBefore(Instant.now())) {
                throw new BadRequestException(String.format(ACCOUNT_LOCKED, userDetails.getLockExpiryDate()));
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


    @Transactional
    @Override
    public User registerUser(RegisterUserRequest req) {

        if (userRepository.existsByUsername(req.getUsername())) {
            throw new BadRequestException(String.format(ALREADY_TAKEN, "username"));
        }

        if (userRepository.existsByEmail(req.getEmail())) {
            throw new BadRequestException(String.format(ALREADY_TAKEN, "email"));
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

        //NB: change server-side link to front-end link
//        var confirmationLink = MvcUriComponentsBuilder
//                .fromMethodName(AuthController.class, "verifyAccount", token)
//                .build().toString();

        //front-end link
         var confirmationLink = frontEndUrlProperties.getConfirmAccount() + token;

        sendConfirmationLink(user, confirmationLink);

        return user;
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

        if (verificationToken.getExpiryDate().isBefore(Instant.now())) {
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

        if (forgottenPassword.getExpiryDate().isBefore(Instant.now())) {
            new BadRequestException(EXPIRED_TOKEN);
        }

        var user = userRepository.findById(forgottenPassword.getUser().getId())
                .orElseThrow(() -> new BadRequestException(INVALID_USER_TOKEN));

        user.setPassword(passwordEncoder.encode(req.getPassword()));
        userRepository.save(user);

        forgottenPasswordRepository.delete(forgottenPassword);
    }

    @Override
    public void changePassword(ResetPasswordRequest req) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        var user = userRepository.findByEmail(authentication.getPrincipal().toString())
                .orElseThrow(() -> new BadRequestException(String.format(NOT_FOUND, "user")));

        user.setPassword(passwordEncoder.encode(req.getPassword()));
        userRepository.save(user);
    }


    @Override
    public UserDetailsImpl getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserDetailsImpl) userDetailsService.loadUserByUsername(authentication.getPrincipal().toString());
    }


    @Override
    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND, "user")));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(NOT_FOUND, "user")));
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
                .orElseThrow(() -> new BadRequestException(INVALID_REFRESH_TOKEN));
        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            throw new BadRequestException(EXPIRED_REFRESH_TOKEN);
        }

        refreshTokenRepository.delete(refreshToken);

        return refreshToken;
    }


}
