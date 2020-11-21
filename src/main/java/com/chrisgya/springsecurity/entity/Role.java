package com.chrisgya.springsecurity.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "roles")
@BatchSize(size = 50)
public class Role extends AbstractEntity implements Serializable {
    @Column(name = "name",unique = true, nullable = false, length = 50)
    private String name;
    @Column(name = "description", length = 100)
    private String description;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "role_permissions",
            joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "permission_id", referencedColumnName = "id", nullable = false, updatable = false)})
    private Set<Permission> permissions = new HashSet<>();
}
