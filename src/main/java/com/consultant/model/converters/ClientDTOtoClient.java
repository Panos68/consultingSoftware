package com.consultant.model.converters;

import com.consultant.model.dto.ClientDTO;
import com.consultant.model.entities.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ClientDTOtoClient implements Converter<ClientDTO, Client> {

    @Override
    public Client convert(ClientDTO clientDTO) {
        Client client = new Client();
        client.setName(clientDTO.getName());
        client.setId(clientDTO.getId());
        client.setConsultantsAssigned(clientDTO.getConsultantsAssigned());
        client.setLastInteractedBy(clientDTO.getLastInteractedBy());
        client.setLastInteractedWith(clientDTO.getLastInteractedWith());
        client.setLastInteractionDate(clientDTO.getLastInteractionDate());
        client.setMainPersonEmail(clientDTO.getMainPersonEmail());
        client.setMainPersonName(clientDTO.getMainPersonName());
        client.setMainPersonPhone(clientDTO.getMainPersonPhone());
        client.setMainTechnologies(clientDTO.getMainTechnologies());
        return client;
    }
}
