package com.consultant.model.repositories;

import com.consultant.model.entities.Vacation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VacationRepository extends JpaRepository<Vacation, Long> {

    @Query(value = "SELECT * FROM vacations v " +
            "JOIN users u " +
            "ON u.id=v.user_id " +
            "WHERE u.id = ?1",
            nativeQuery = true)
    List<Vacation> findByUserId(Long userId);
}
