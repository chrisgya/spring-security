package com.chrisgya.springsecurity.entity;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@ApiResponse
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "roles", schema = "bp")
@BatchSize(size = 50)
public class Role implements Serializable {
    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at",updatable = false)
    private Instant createdAt;

    @Column(name = "created_by", nullable = false, length = 50)
    private String createdBy;

    @Version
    private long version;

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
