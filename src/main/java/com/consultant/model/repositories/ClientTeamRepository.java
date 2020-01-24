package com.consultant.model.repositories;

import com.consultant.model.entities.ClientTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientTeamRepository extends JpaRepository<ClientTeam, Long> {

}
