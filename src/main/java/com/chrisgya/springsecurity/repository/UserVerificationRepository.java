package com.chrisgya.springsecurity.repository;

import com.chrisgya.springsecurity.entity.UserVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserVerificationRepository extends JpaRepository<UserVerification, Long> {

    Optional<UserVerification> findByToken(String token);
}
