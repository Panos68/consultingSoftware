package com.consultant.model.security;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthenticationUserRepository extends JpaRepository<AuthenticationUser, Long> {

    Optional<AuthenticationUser> findByUsername(String username);

}
