package com.consultant.model.converters.client;

import com.consultant.model.dto.ClientCompanyDTO;
import com.consultant.model.entities.ClientCompany;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DTOtoClientCompany implements Converter<ClientCompanyDTO, ClientCompany> {

    @Override
    public ClientCompany convert(ClientCompanyDTO clientCompanyDTO) {
        ClientCompany clientCompany = new ClientCompany();

        clientCompany.setName(clientCompanyDTO.getName());
        clientCompany.setLastInteractedBy(clientCompanyDTO.getLastInteractedBy());
        clientCompany.setLastInteractedWith(clientCompanyDTO.getLastInteractedWith());
        clientCompany.setLastInteractionDate(clientCompanyDTO.getLastInteractionDate());
        clientCompany.setMainPersonEmail(clientCompanyDTO.getMainPersonEmail());
        clientCompany.setMainPersonName(clientCompanyDTO.getMainPersonName());
        clientCompany.setMainPersonPhone(clientCompanyDTO.getMainPersonPhone());
        clientCompany.setClientTeams(clientCompanyDTO.getClientTeams());
        if (clientCompanyDTO.getId() != null) {
            clientCompany.setId(clientCompanyDTO.getId());
        }

        return clientCompany;
    }
}
