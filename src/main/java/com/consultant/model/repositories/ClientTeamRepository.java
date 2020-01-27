package com.consultant.model.repositories;

import com.consultant.model.entities.ClientTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientTeamRepository extends JpaRepository<ClientTeam, Long> {

 @Query(value = "SELECT * FROM CLIENT_TEAMS ct " +
                 "JOIN CONSULTANTS c " +
                 "ON c.team_id=ct.id " +
                 "WHERE c.id = ?1",
         nativeQuery = true)
 ClientTeam findByConsultantId(Long consultantId);
}
