package com.chrisgya.springsecurity.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "users", schema = "bp")
public class User extends AbstractEntity implements Serializable {
    @Column(name = "username",unique = true, nullable = false, length = 50)
    private String username;
    @Column(name = "email",unique = true, nullable = false, length = 75)
    private String email;
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;
    @Column(name = "middle_name", length = 50)
    private String middleName;
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;
    @Column(name = "picture_url")
    private String pictureUrl;
    @Column(name = "password", nullable = false, length = 1000)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @Column(name = "is_locked")
    private boolean isLocked;
    @Column(name = "lock_expiry_date")
    private Instant lockExpiryDate;
    @Column(name = "is_enabled")
    private boolean isEnabled;
    @Column(name = "is_confirmed")
    private boolean isConfirmed;
    @Column(name = "last_updated")
    private Instant lastUpdated;

//    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
//    @JoinTable(name = "user_roles",
//            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, updatable = false)},
//            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false, updatable = false)})
//    private Set<Role> roles = new HashSet<>();

//
//
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<UserRoles> userRoles = new HashSet<>();
//
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", referencedColumnName = "id")
//    private UserVerification userVerification;

}
