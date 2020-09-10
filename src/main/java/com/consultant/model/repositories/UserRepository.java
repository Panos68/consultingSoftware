package com.consultant.model.repositories;

import com.consultant.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query(value = "SELECT * FROM users u " +
            "JOIN vacations v " +
            "ON v.user_id=u.id " +
            "WHERE v.id = ?1",
            nativeQuery = true)
    Optional<User> findByVacationId(Long vacationId);
}
