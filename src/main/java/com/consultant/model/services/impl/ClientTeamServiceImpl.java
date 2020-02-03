package com.consultant.model.services.impl;

import com.consultant.model.dto.ClientTeamDTO;
import com.consultant.model.entities.Client;
import com.consultant.model.entities.ClientTeam;
import com.consultant.model.entities.Consultant;
import com.consultant.model.exception.NoMatchException;
import com.consultant.model.mappers.AbstractClientMapper;
import com.consultant.model.repositories.ClientTeamRepository;
import com.consultant.model.services.ClientService;
import com.consultant.model.services.ClientTeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ClientTeamServiceImpl implements ClientTeamService {

    @Autowired
    ClientTeamRepository clientTeamRepository;

    @Autowired
    ClientService clientService;

    @Override
    public Set<ClientTeamDTO> getAllTeams() {
        List<ClientTeam> clientTeamsList = clientTeamRepository.findAll();
        Set<ClientTeamDTO> clientTeamsDTOS = new HashSet<>();
        clientTeamsList.forEach(team -> {
            setClientOfTeam(team);
            final ClientTeamDTO clientTeamDTO = AbstractClientMapper.INSTANCE.clientTeamToClientTeamDTO(team);
            clientTeamsDTOS.add(clientTeamDTO);
        });

        return clientTeamsDTOS;
    }

    private void setClientOfTeam(ClientTeam team) {
        Optional<Client> clientOfTeam = clientService.getClientOfTeam(team.getId());
        if (clientOfTeam.isPresent()) {
            team.setClientId(clientOfTeam.get().getId());
            team.setClientName(clientOfTeam.get().getName());
        }
    }

    @Override
    public void createTeam(ClientTeamDTO clientTeamDTO) throws NoMatchException {
        ClientTeam clientTeam = AbstractClientMapper.INSTANCE.clientTeamDTOToClientTeam(clientTeamDTO);

        assignTeamToClient(clientTeamDTO.getClientId(), clientTeam);
    }

    /**
     * Checks if an id was sent to be saved for the team. If exists it assigns the team to that company, if not it just
     * saves the updated team.
     *
     * @param clientId   the client id to be assigned to
     * @param clientTeam the updated team to assign and save
     * @throws NoMatchException
     */
    private void assignTeamToClient(Long clientId, ClientTeam clientTeam) throws NoMatchException {
        if (clientId != null) {
            clientService.assignTeamToClient(clientTeam, clientId);
        } else {
            clientTeamRepository.saveAndFlush(clientTeam);
        }
    }

    @Override
    public void editTeam(ClientTeamDTO clientTeamDTO) throws NoMatchException {
        ClientTeam existingClient = getExistingTeamById(clientTeamDTO.getId());

        ClientTeam updatedClientTeam = updateTeam(existingClient, clientTeamDTO);

        assignTeamToClient(clientTeamDTO.getClientId(), updatedClientTeam);
    }

    @Override
    public void deleteTeam(Long id) throws NoMatchException {
        ClientTeam existingTeam = getExistingTeamById(id);
        existingTeam.setConsultants(Collections.emptyList());
        clientTeamRepository.saveAndFlush(existingTeam);
        clientTeamRepository.delete(existingTeam);
    }

    @Override
    public void assignConsultantToTeam(Consultant consultant, Long teamId) throws NoMatchException {
        ClientTeam clientTeam = getExistingTeamById(teamId);
        clientTeam.getConsultants().add(consultant);
        clientTeamRepository.saveAndFlush(clientTeam);
    }

    @Override
    public Optional<ClientTeam> getAssignedTeamOfConsultant(Long consultantId) {
        Optional<ClientTeam> clientTeam = clientTeamRepository.findByConsultantId(consultantId);
        return clientTeam;
    }

    private ClientTeam updateTeam(ClientTeam existingClientTeam, ClientTeamDTO clientTeamDTO) {
        existingClientTeam.setName(clientTeamDTO.getName());
        existingClientTeam.setId(clientTeamDTO.getId());
        existingClientTeam.setLastInteractedBy(clientTeamDTO.getLastInteractedBy());
        existingClientTeam.setLastInteractedWith(clientTeamDTO.getLastInteractedWith());
        existingClientTeam.setLastInteractionDate(clientTeamDTO.getLastInteractionDate());
        existingClientTeam.setMainPersonEmail(clientTeamDTO.getMainPersonEmail());
        existingClientTeam.setMainPersonName(clientTeamDTO.getMainPersonName());
        existingClientTeam.setMainPersonPhone(clientTeamDTO.getMainPersonPhone());
        existingClientTeam.setConsultants(clientTeamDTO.getConsultants());
        existingClientTeam.setMainTechnologies(clientTeamDTO.getMainTechnologies());
        return existingClientTeam;
    }

    private ClientTeam getExistingTeamById(Long id) throws NoMatchException {
        Optional<ClientTeam> existingTeam = clientTeamRepository.findById(id);
        if (!existingTeam.isPresent()) {
            throw new NoMatchException("The id provided doesn't match any team");
        }

        return existingTeam.get();
    }
}
