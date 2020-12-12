package com.chrisgya.springsecurity.dao;

import com.chrisgya.springsecurity.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final String INSERT_USER_QUERY = "INSERT INTO USER(id,first_name,last_name,gender,age) VALUES(?,?,?,?,?)";
    private final String UPDATE_USER_QUERY = "UPDATE user SET first_name=?,last_name=?,gender=?,age=? WHERE id=?";
    private final String DELETE_USER_QUERY = "DELETE FROM user WHERE id=?";
    private final String GET_USER_BY_ID_QUERY = "SELECT * FROM user where id = ?";
    private final String GET_USERS_QUERY = "SELECT * FROM user";
    private final String GET_USER_PERMISSIONS_QUERY = "SELECT name FROM permissions p JOIN role_permissions rp ON p.id=rp.permission_id JOIN user_roles ur ON rp.role_id= ur.role_id WHERE user_id=?";

    @Override
    public int save(User user) {

        return jdbcTemplate.update(INSERT_USER_QUERY, new Object[] { user.getId(), user.getFirstName(),
                user.getLastName(), user.getEmail(), user.getUsername() });
    }

    @Override
    public int update(User user) {

        return jdbcTemplate.update(UPDATE_USER_QUERY, new Object[] { user.getFirstName(), user.getLastName(),
                user.getEmail(), user.getUsername(), user.getId() });
    }

    @Override
    public int delete(int id) {

        return jdbcTemplate.update(DELETE_USER_QUERY, new Object[] { id });
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(GET_USERS_QUERY, new UserRowMapper());
    }

    @Override
    public List<String> findUserPermissions(Long userId) {
        return jdbcTemplate.query(GET_USER_PERMISSIONS_QUERY, new UserPermissionRowMapper(), new Object[] { userId });
    }

    @Override
    public Optional<User> findById(int id) {

        return Optional.of(jdbcTemplate.queryForObject(GET_USER_BY_ID_QUERY, new UserRowMapper(), new Object[] {id}));
    }
}
