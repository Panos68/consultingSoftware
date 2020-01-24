package com.consultant.model.services;

import com.consultant.model.dto.ClientTeamDTO;
import com.consultant.model.exception.NoMatchException;

import java.util.Set;

public interface ClientTeamService {
    Set<ClientTeamDTO> getAllTeams();

    void createTeam(ClientTeamDTO clientTeamDTO) throws NoMatchException;

    void editTeam(ClientTeamDTO clientTeamDTO) throws NoMatchException;

    void deleteTeam(Long id) throws NoMatchException;
}
