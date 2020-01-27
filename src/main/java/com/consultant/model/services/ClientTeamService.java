package com.consultant.model.services;

import com.consultant.model.dto.ClientTeamDTO;
import com.consultant.model.entities.ClientTeam;
import com.consultant.model.entities.Consultant;
import com.consultant.model.exception.NoMatchException;

import java.util.Optional;
import java.util.Set;

public interface ClientTeamService {
    Set<ClientTeamDTO> getAllTeams();

    void createTeam(ClientTeamDTO clientTeamDTO) throws NoMatchException;

    void editTeam(ClientTeamDTO clientTeamDTO) throws NoMatchException;

    void deleteTeam(Long id) throws NoMatchException;

    void assignConsultantToTeam(Consultant consultant, Long teamId) throws NoMatchException;

    /** Gets the id of a consultant and returns his team if he is assigned to one and null if he isn't
     * @param consultantId the id of the consultant
     * @return the id of the consultant team
     */
    Optional<ClientTeam> getAssignedTeamOfConsultant(Long consultantId);
}
