package com.chrisgya.springsecurity.repository;

import com.chrisgya.springsecurity.entity.RefreshToken;
import com.chrisgya.springsecurity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUser(User user);
}
