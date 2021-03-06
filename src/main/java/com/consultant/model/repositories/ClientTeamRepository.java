package com.consultant.model.repositories;

import com.consultant.model.entities.ClientTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientTeamRepository extends JpaRepository<ClientTeam, Long> {

    @Query(value = "SELECT * FROM client_teams ct " +
            "JOIN consultants c " +
            "ON c.team_id=ct.id " +
            "WHERE c.id = ?1",
            nativeQuery = true)
    Optional<ClientTeam> findByConsultantId(Long consultantId);

    Optional<ClientTeam> findTeamByName(String name);
}
