package com.consultant.model.services;

import com.consultant.model.dto.ClientDTO;
import com.consultant.model.entities.Client;
import com.consultant.model.entities.ClientTeam;
import com.consultant.model.exception.NoMatchException;

import java.util.Optional;
import java.util.Set;

public interface ClientService {
    Set<ClientDTO> getAllClients();

    void createClient(ClientDTO clientDTO);

    void editClient(ClientDTO clientDTO) throws NoMatchException;

    void deleteClient(Long id) throws NoMatchException;

    void assignTeamToClient(ClientTeam clientTeam, Long clientId) throws NoMatchException;

    /** Gets the id of a team and returns his client if he is assigned to one and empty optional if he isn't
     * @param teamId the id of the team
     * @return client company that he is assigned or optional empty if he is not
     */
    Optional<Client> getClientOfTeam(Long teamId);
}
