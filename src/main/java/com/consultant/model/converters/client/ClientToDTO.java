package com.consultant.model.converters.client;

import com.consultant.model.dto.ClientDTO;
import com.consultant.model.entities.Client;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ClientToDTO implements Converter<Client, ClientDTO> {
    @Override
    public ClientDTO convert(Client client) {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setName(client.getName());
        clientDTO.setId(client.getId());
        clientDTO.setLastInteractedBy(client.getLastInteractedBy());
        clientDTO.setLastInteractedWith(client.getLastInteractedWith());
        clientDTO.setLastInteractionDate(client.getLastInteractionDate());
        clientDTO.setMainPersonEmail(client.getMainPersonEmail());
        clientDTO.setMainPersonName(client.getMainPersonName());
        clientDTO.setMainPersonPhone(client.getMainPersonPhone());
        clientDTO.setClientTeams(client.getClientTeams());

        return clientDTO;
    }
}
