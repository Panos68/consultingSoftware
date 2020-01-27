package com.consultant.model.converters.client;

import com.consultant.model.dto.ClientDTO;
import com.consultant.model.entities.Client;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DTOtoClient implements Converter<ClientDTO, Client> {

    @Override
    public Client convert(ClientDTO clientDTO) {
        Client client = new Client();

        client.setName(clientDTO.getName());
        client.setLastInteractedBy(clientDTO.getLastInteractedBy());
        client.setLastInteractedWith(clientDTO.getLastInteractedWith());
        client.setLastInteractionDate(clientDTO.getLastInteractionDate());
        client.setMainPersonEmail(clientDTO.getMainPersonEmail());
        client.setMainPersonName(clientDTO.getMainPersonName());
        client.setMainPersonPhone(clientDTO.getMainPersonPhone());
        client.setClientTeams(clientDTO.getClientTeams());
        if (clientDTO.getId() != null) {
            client.setId(clientDTO.getId());
        }

        return client;
    }
}
