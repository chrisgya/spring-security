package com.chrisgya.springsecurity.model;

import com.chrisgya.springsecurity.entity.User;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class UserDetailsImpl implements UserDetails {
    private long id;
    private String username;
    private String email;
    private String firstName;
    private String middleName;
    private String lastname;
    //@JsonIgnore
    private String password;
    private boolean isLocked;
    private LocalDate lockExpiryDate;
    private boolean isEnabled;
    private boolean isConfirmed;
    private Collection<? extends GrantedAuthority> authorities;

    public static UserDetailsImpl build(User user, List<String> permissions) {

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

//        user.getRoles().stream()
//                .forEach(role -> {
//                    role.getPermissions().stream()
//                            .forEach(p -> authorities.add(new SimpleGrantedAuthority(p.getName())));
//
//                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
//
//                });


        permissions.forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission)));

        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getMiddleName(),
                user.getLastName(),
                user.getPassword(),
                user.isLocked(),
                user.getLockExpiryDate(),
                user.isEnabled(),
                user.isConfirmed(),
                authorities
        );
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
