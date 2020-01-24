package com.consultant.model.services.impl;

import com.consultant.model.dto.ClientTeamDTO;
import com.consultant.model.entities.ClientTeam;
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
        clientCompanyService.assignTeamToCompany(clientTeam,clientTeamDTO.getClientId());
        clientTeamRepository.saveAndFlush(clientTeam);
    }

    @Override
    public void editTeam(ClientTeamDTO clientTeamDTO) throws NoMatchException {
        Optional<ClientTeam> existingClient = getExistingTeamById(clientTeamDTO.getId());

        ClientTeam updatedClientTeam = updateTeam(existingClient.get(), clientTeamDTO);

        clientTeamRepository.saveAndFlush(updatedClientTeam);
    }

    @Override
    public void deleteTeam(Long id) throws NoMatchException {
        Optional<ClientTeam> existingTeam = getExistingTeamById(id);

        clientTeamRepository.delete(existingTeam.get());
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
        existingClientTeam.setConsultantsAssigned(clientTeamDTO.getConsultantsAssigned());
        existingClientTeam.setMainTechnologies(clientTeamDTO.getMainTechnologies());
        return existingClientTeam;
    }

    private Optional<ClientTeam> getExistingTeamById(Long id) throws NoMatchException {
        Optional<ClientTeam> existingTeam = clientTeamRepository.findById(id);
        if (!existingTeam.isPresent()) {
            throw new NoMatchException("The id provided doesn't match any team");
        }

        return existingTeam;
    }
}
