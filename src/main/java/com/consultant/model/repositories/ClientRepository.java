package com.consultant.model.repositories;

import com.consultant.model.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByName(String name);

    @Query(value = "SELECT * FROM clients c " +
            "JOIN client_teams ct " +
            "ON ct.client_id=c.id " +
            "WHERE ct.id = ?1",
            nativeQuery = true)
    Optional<Client> findByTeamId(Long teamId);
}
