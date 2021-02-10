package com.chrisgya.springsecurity.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;
import java.time.Instant;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "users", schema = "bp",
        uniqueConstraints = {
                @UniqueConstraint(name = "users_username_unique", columnNames = "username"),
                @UniqueConstraint(name = "users_email_unique", columnNames = "email")
        })
public class User extends AbstractEntity implements Serializable {
    @Column(name = "username", nullable = false, length = 50)
    private String username;
    @Column(name = "email", nullable = false, length = 75)
    private String email;
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;
    @Column(name = "middle_name", length = 50)
    private String middleName;
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;
    @Column(name = "picture_url", columnDefinition = "TEXT")
    private String pictureUrl;
    @Column(name = "password", nullable = false, length = 1000)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @Column(name = "locked")
    private boolean locked;
    @Column(name = "lock_expired_at")
    private Instant lockExpiredAt;
    @Column(name = "enabled")
    private boolean enabled;
    @Column(name = "confirmed")
    private boolean confirmed;
    @Column(name = "last_updated_at")
    private Instant lastUpdatedAt;

//    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
//    @JoinTable(name = "user_roles",
//            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, updatable = false)},
//            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false, updatable = false)})
//    private Set<Role> roles = new HashSet<>();

    //
//
//    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval=true)
//    private Set<UserRoles> userRoles = new HashSet<>();
//
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", referencedColumnName = "id")
//    private UserVerification userVerification;

}
