package com.chrisgya.springsecurity.repository;

import com.chrisgya.springsecurity.entity.ForgottenPassword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ForgottenPasswordRepository extends JpaRepository<ForgottenPassword, Long> {

    Optional<ForgottenPassword> findByToken(String token);
}
