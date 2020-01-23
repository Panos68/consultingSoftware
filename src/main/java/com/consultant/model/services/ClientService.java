package com.consultant.model.services;

import com.consultant.model.dto.ClientDTO;
import com.consultant.model.exception.NoMatchException;

import java.util.Set;

public interface ClientService {
    Set<ClientDTO> getAllClients();

    void createClient(ClientDTO clientDTO);

    void editClient(ClientDTO clientDTO) throws NoMatchException;

    void deleteClient(Long id) throws NoMatchException;
}
