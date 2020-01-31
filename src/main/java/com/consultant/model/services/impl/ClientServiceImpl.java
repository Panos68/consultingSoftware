package com.consultant.model.services.impl;

import com.consultant.model.dto.ClientDTO;
import com.consultant.model.entities.Client;
import com.consultant.model.entities.ClientTeam;
import com.consultant.model.exception.EntityAlreadyExists;
import com.consultant.model.exception.NoMatchException;
import com.consultant.model.repositories.ClientRepository;
import com.consultant.model.services.ClientService;
import com.consultant.model.services.ClientTeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    ConversionService conversionService;

    @Autowired
    ClientTeamService clientTeamService;

    @Override
    public Set<ClientDTO> getAllClients() {
        List<Client> clientList = clientRepository.findAll();
        Set<ClientDTO> clientDTOS = new HashSet<>();
        clientList.forEach(client -> {
            final ClientDTO clientDTO = conversionService.convert(client, ClientDTO.class);
            clientDTOS.add(clientDTO);
        });

        return clientDTOS;
    }

    @Override
    public void createClient(ClientDTO clientDTO) {
        Optional<Client> existingClient = clientRepository.findByName(clientDTO.getName());
        if (existingClient.isPresent()) {
            throw new EntityAlreadyExists("Client company already exists");
        }

        final Client client = conversionService.convert(clientDTO, Client.class);

        clientRepository.saveAndFlush(client);
    }

    @Override
    public void editClient(ClientDTO clientDTO) throws NoMatchException {
        Client existingClient = getExistingClientById(clientDTO.getId());

        Client updatedClient = updateClient(existingClient, clientDTO);

        clientRepository.saveAndFlush(updatedClient);
    }

    @Override
    public void deleteClient(Long id) throws NoMatchException {
        Client existingClient = getExistingClientById(id);
        List<ClientTeam> clientTeams = existingClient.getClientTeams();
        clientTeams.forEach(clientTeam -> {
            try {
                clientTeamService.deleteTeam(clientTeam.getId());
            } catch (NoMatchException e) {
                e.printStackTrace();
            }
        });

        clientRepository.delete(existingClient);
    }

    @Override
    public void assignTeamToClient(ClientTeam clientTeam, Long clientId) throws NoMatchException {
        Client existingClient = getExistingClientById(clientId);
        existingClient.getClientTeams().add(clientTeam);
        clientRepository.saveAndFlush(existingClient);
    }

    @Override
    public Optional<Client> getClientOfTeam(Long teamId) {
        return clientRepository.findByTeamId(teamId);
    }

    private Client updateClient(Client existingClient, ClientDTO clientDTO) {
        existingClient.setName(clientDTO.getName());
        existingClient.setId(clientDTO.getId());
        existingClient.setLastInteractedBy(clientDTO.getLastInteractedBy());
        existingClient.setLastInteractedWith(clientDTO.getLastInteractedWith());
        existingClient.setLastInteractionDate(clientDTO.getLastInteractionDate());
        existingClient.setMainPersonEmail(clientDTO.getMainPersonEmail());
        existingClient.setMainPersonName(clientDTO.getMainPersonName());
        existingClient.setMainPersonPhone(clientDTO.getMainPersonPhone());
        return existingClient;
    }

    private Client getExistingClientById(Long id) throws NoMatchException {
        Optional<Client> existingClient = clientRepository.findById(id);
        if (!existingClient.isPresent()) {
            throw new NoMatchException("The id provided doesn't match any client");
        }

        return existingClient.get();
    }
}
