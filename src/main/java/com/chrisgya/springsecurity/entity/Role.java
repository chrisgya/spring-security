package com.chrisgya.springsecurity.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "roles", schema = "bp")
@BatchSize(size = 50)
public class Role extends AbstractEntity implements Serializable {
    @Column(name = "name",unique = true, nullable = false, length = 50)
    private String name;

    @Column(name = "description", length = 100)
    private String description;

//    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private Set<UserRoles> userRoles = new HashSet<>();
//
//    @OneToMany(mappedBy = "role_permissions", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private Set<RolePermissions> rolePermissions = new HashSet<>();
}
