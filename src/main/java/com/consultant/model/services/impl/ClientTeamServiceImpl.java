package com.consultant.model.services.impl;

import com.consultant.model.dto.ClientTeamDTO;
import com.consultant.model.entities.ClientTeam;
import com.consultant.model.entities.Consultant;
import com.consultant.model.exception.NoMatchException;
import com.consultant.model.repositories.ClientTeamRepository;
import com.consultant.model.services.ClientCompanyService;
import com.consultant.model.services.ClientTeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ClientTeamServiceImpl implements ClientTeamService {

    ClientTeamRepository clientTeamRepository;

    ClientCompanyService clientCompanyService;

    ConversionService conversionService;

    @Autowired
    public ClientTeamServiceImpl(ClientTeamRepository clientTeamRepository, ClientCompanyService clientCompanyService, ConversionService conversionService) {
        this.clientTeamRepository = clientTeamRepository;
        this.clientCompanyService = clientCompanyService;
        this.conversionService = conversionService;
    }

    @Override
    public Set<ClientTeamDTO> getAllTeams() {
        List<ClientTeam> clientCompanyList = clientTeamRepository.findAll();
        Set<ClientTeamDTO> clientCompanyDTOS = new HashSet<>();
        clientCompanyList.forEach(client -> {
            final ClientTeamDTO clientCompanyDTO = conversionService.convert(client, ClientTeamDTO.class);
            clientCompanyDTOS.add(clientCompanyDTO);
        });

        return clientCompanyDTOS;
    }

    @Override
    public void createTeam(ClientTeamDTO clientTeamDTO) throws NoMatchException {
        final ClientTeam clientTeam = conversionService.convert(clientTeamDTO, ClientTeam.class);

        assignTeamToCompany(clientTeamDTO.getClientId(), clientTeam);
    }

    /**
     * Checks if an id was sent to be saved for the team. If exists it assigns the team to that company, if not it just
     * saves the updated team.
     * @param clientId
     * @param clientTeam
     * @throws NoMatchException
     */
    private void assignTeamToCompany(Long clientId, ClientTeam clientTeam) throws NoMatchException {
        if (clientId != null) {
            clientCompanyService.assignTeamToCompany(clientTeam, clientId);
        } else {
            clientTeamRepository.saveAndFlush(clientTeam);
        }
    }

    @Override
    public void editTeam(ClientTeamDTO clientTeamDTO) throws NoMatchException {
        ClientTeam existingClient = getExistingTeamById(clientTeamDTO.getId());

        ClientTeam updatedClientTeam = updateTeam(existingClient, clientTeamDTO);

        assignTeamToCompany(clientTeamDTO.getClientId(), updatedClientTeam);
    }

    @Override
    public void deleteTeam(Long id) throws NoMatchException {
        ClientTeam existingTeam = getExistingTeamById(id);

        clientTeamRepository.delete(existingTeam);
    }

    @Override
    public void assignConsultantToTeam(Consultant consultant, Long teamId) throws NoMatchException {
        ClientTeam clientTeam = getExistingTeamById(teamId);
        clientTeam.getConsultants().add(consultant);
        clientTeamRepository.saveAndFlush(clientTeam);
    }

    @Override
    public ClientTeam getAssignedTeamOfConsultant(Long consultantId) {
        ClientTeam clientTeam = clientTeamRepository.findByConsultantId(consultantId);
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
