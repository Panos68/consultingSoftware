package com.consultant.model.repositories;

import com.consultant.model.entities.ClientCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientCompanyRepository extends JpaRepository<ClientCompany, Long> {

    Optional<ClientCompany> findByName(String name);

    @Query(value = "SELECT * FROM CLIENTS c " +
            "JOIN CLIENT_TEAMS ct " +
            "ON ct.client_id=c.id " +
            "WHERE ct.id = ?1",
            nativeQuery = true)
    Optional<ClientCompany> findByTeamId(Long teamId);
}
