package com.consultant.model.services.impl;

import com.consultant.model.dto.ClientCompanyDTO;
import com.consultant.model.entities.ClientCompany;
import com.consultant.model.entities.ClientTeam;
import com.consultant.model.exception.EntityAlreadyExists;
import com.consultant.model.exception.NoMatchException;
import com.consultant.model.repositories.ClientCompanyRepository;
import com.consultant.model.services.ClientCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ClientCompanyServiceImpl implements ClientCompanyService {

    ClientCompanyRepository clientCompanyRepository;

    ConversionService conversionService;

    @Autowired
    public ClientCompanyServiceImpl(ClientCompanyRepository clientCompanyRepository, ConversionService conversionService) {
        this.clientCompanyRepository = clientCompanyRepository;
        this.conversionService = conversionService;
    }


    @Override
    public Set<ClientCompanyDTO> getAllCompanies() {
        List<ClientCompany> clientCompanyList = clientCompanyRepository.findAll();
        Set<ClientCompanyDTO> clientCompanyDTOS = new HashSet<>();
        clientCompanyList.forEach(client -> {
            final ClientCompanyDTO clientCompanyDTO = conversionService.convert(client, ClientCompanyDTO.class);
            clientCompanyDTOS.add(clientCompanyDTO);
        });

        return clientCompanyDTOS;
    }

    @Override
    public void createCompany(ClientCompanyDTO clientCompanyDTO) {
        Optional<ClientCompany> existingClient = clientCompanyRepository.findByName(clientCompanyDTO.getName());
        if (existingClient.isPresent()) {
            throw new EntityAlreadyExists("Client company already exists");
        }

        final ClientCompany clientCompany = conversionService.convert(clientCompanyDTO, ClientCompany.class);

        clientCompanyRepository.saveAndFlush(clientCompany);
    }

    @Override
    public void editCompany(ClientCompanyDTO clientCompanyDTO) throws NoMatchException {
        ClientCompany existingClient = getExistingClientById(clientCompanyDTO.getId());

        ClientCompany updatedClientCompany = updateClient(existingClient, clientCompanyDTO);

        clientCompanyRepository.saveAndFlush(updatedClientCompany);
    }

    @Override
    public void deleteCompany(Long id) throws NoMatchException {
        ClientCompany existingClient = getExistingClientById(id);

        clientCompanyRepository.delete(existingClient);
    }

    @Override
    public void assignTeamToCompany(ClientTeam clientTeam, Long companyId) throws NoMatchException {
        if (companyId != null) {
            ClientCompany existingClient = getExistingClientById(companyId);
            existingClient.getClientTeams().add(clientTeam);
            clientCompanyRepository.saveAndFlush(existingClient);
        }
    }

    private ClientCompany updateClient(ClientCompany existingClientCompany, ClientCompanyDTO clientCompanyDTO) {
        existingClientCompany.setName(clientCompanyDTO.getName());
        existingClientCompany.setId(clientCompanyDTO.getId());
        existingClientCompany.setLastInteractedBy(clientCompanyDTO.getLastInteractedBy());
        existingClientCompany.setLastInteractedWith(clientCompanyDTO.getLastInteractedWith());
        existingClientCompany.setLastInteractionDate(clientCompanyDTO.getLastInteractionDate());
        existingClientCompany.setMainPersonEmail(clientCompanyDTO.getMainPersonEmail());
        existingClientCompany.setMainPersonName(clientCompanyDTO.getMainPersonName());
        existingClientCompany.setMainPersonPhone(clientCompanyDTO.getMainPersonPhone());
        return existingClientCompany;
    }

    private ClientCompany getExistingClientById(Long id) throws NoMatchException {
        Optional<ClientCompany> existingClient = clientCompanyRepository.findById(id);
        if (!existingClient.isPresent()) {
            throw new NoMatchException("The id provided doesn't match any client");
        }

        return existingClient.get();
    }
}
