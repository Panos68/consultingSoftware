package com.consultant.model.services.impl;

import com.consultant.model.dto.ClientDTO;
import com.consultant.model.entities.Client;
import com.consultant.model.entities.ClientTeam;
import com.consultant.model.exception.EntityAlreadyExists;
import com.consultant.model.exception.NoMatchException;
import com.consultant.model.mappers.AbstractClientMapper;
import com.consultant.model.repositories.ClientRepository;
import com.consultant.model.services.BasicOperationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ClientService implements BasicOperationsService<ClientDTO> {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    ClientTeamService clientTeamService;

    @Override
    public Set<ClientDTO> getAll() {
        List<Client> clientList = clientRepository.findAll();
        Set<ClientDTO> clientDTOS = new LinkedHashSet<>();
        clientList.forEach(client -> {
            final ClientDTO clientDTO = AbstractClientMapper.INSTANCE.clientToClientDTO(client);
            clientDTOS.add(clientDTO);
        });

        return clientDTOS;
    }

    @Override
    public void create(ClientDTO clientDTO) throws NoMatchException {
        Optional<Client> existingClient = clientRepository.findByName(clientDTO.getName());
        if (existingClient.isPresent()) {
            throw new EntityAlreadyExists("Client company already exists");
        }
        final Client client = AbstractClientMapper.INSTANCE.clientDTOToClient(clientDTO);
        client.setDeleted(false);

        clientRepository.saveAndFlush(client);
    }

    @Override
    public void edit(ClientDTO clientDTO) throws NoMatchException {
        Client existingClient = getExistingClientById(clientDTO.getId());

        Client updatedClient = updateClient(existingClient, clientDTO);

        clientRepository.saveAndFlush(updatedClient);
    }

    @Override
    public void delete(Long id) throws NoMatchException {
        Client existingClient = getExistingClientById(id);
        List<ClientTeam> clientTeams = existingClient.getClientTeams();
        clientTeams.forEach(clientTeam -> {
            try {
                clientTeamService.delete(clientTeam.getId());
            } catch (NoMatchException e) {
                e.printStackTrace();
            }
        });
        existingClient.setDeleted(true);

        clientRepository.saveAndFlush(existingClient);
    }

    public void assignTeamToClient(ClientTeam clientTeam, Long clientId) throws NoMatchException {
        Client existingClient = getExistingClientById(clientId);
        existingClient.getClientTeams().add(clientTeam);
        setLastInteraction(clientTeam, existingClient);

        clientRepository.saveAndFlush(existingClient);
    }

    public Optional<Client> getClientOfTeam(Long teamId) {
        return clientRepository.findByTeamId(teamId);
    }

    private void setLastInteraction(ClientTeam clientTeam, Client existingClient) {
        if (existingClient.getLastInteractionDate() != null && clientTeam.getLastInteractionDate() != null &&
                existingClient.getLastInteractionDate().isBefore(clientTeam.getLastInteractionDate())) {
            existingClient.setLastInteractedWith(clientTeam.getLastInteractedWith());
            existingClient.setLastInteractedBy(clientTeam.getLastInteractedBy());
            existingClient.setLastInteractionDate(clientTeam.getLastInteractionDate());
        }
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
