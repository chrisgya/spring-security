package com.chrisgya.springsecurity.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "role_permissions", schema = "bp", uniqueConstraints = {
        @UniqueConstraint(name = "permission_role_unique", columnNames = {"permission_id", "role_id"})
})
public class RolePermissions extends AbstractEntity implements Serializable {

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "permission_id", referencedColumnName = "id")
    private Permission permission;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;

    public RolePermissions(Permission permission, Role role, String createdBy) {
        this.permission = permission;
        this.role = role;
        this.setCreatedBy(createdBy);
    }
}
