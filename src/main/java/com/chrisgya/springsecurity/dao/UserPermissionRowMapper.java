package com.chrisgya.springsecurity.dao;

import com.chrisgya.springsecurity.entity.Permission;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserPermissionRowMapper implements RowMapper<Permission> {

    @Override
    public Permission mapRow(ResultSet rs, int rowNum) throws SQLException {
        var permission = new Permission(rs.getString("name"), rs.getString("description"));
        permission.setId(rs.getLong("id"));
        permission.setCreatedBy(rs.getString("created_by"));
      //  permission.setCreatedAt(Instant.parse(rs.getString("created_at")));

        return permission;
    }
}