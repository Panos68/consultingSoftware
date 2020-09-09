package com.consultant.model.repositories;

import com.consultant.model.entities.Oauth2User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OauthUserRepository extends JpaRepository<Oauth2User, Long> {

    Optional<Oauth2User> findByEmail(String email);
}
