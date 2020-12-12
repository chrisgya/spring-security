package com.chrisgya.springsecurity.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "users")
@BatchSize(size = 50)
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
    @Column(name = "password", nullable = false, length = 1000)
    private String password;
    @Column(name = "is_locked")
    private boolean isLocked;
    @Column(name = "lock_expiry_date")
    private LocalDate lockExpiryDate;
    @Column(name = "is_enabled")
    private boolean isEnabled;
    @Column(name = "is_confirmed")
    private boolean isConfirmed;


//    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
//    @JoinTable(name = "user_roles",
//            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, updatable = false)},
//            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false, updatable = false)})
//    private Set<Role> roles = new HashSet<>();



    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<UserRoles> userRoles = new HashSet<>();
}
