package com.chrisgya.springsecurity.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "role_permissions", schema = "bp", uniqueConstraints = {
        @UniqueConstraint(name = "permission_role_unique", columnNames = {"permission_id", "role_id"})
})
public class RolePermissions extends AbstractEntity{

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "permission_id", referencedColumnName = "id")
    private Permission permission;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;

}
