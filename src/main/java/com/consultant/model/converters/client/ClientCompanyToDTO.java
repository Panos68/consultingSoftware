package com.consultant.model.converters.client;

import com.consultant.model.dto.ClientCompanyDTO;
import com.consultant.model.entities.ClientCompany;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ClientCompanyToDTO implements Converter<ClientCompany, ClientCompanyDTO> {
    @Override
    public ClientCompanyDTO convert(ClientCompany clientCompany) {
        ClientCompanyDTO clientCompanyDTO = new ClientCompanyDTO();
        clientCompanyDTO.setName(clientCompany.getName());
        clientCompanyDTO.setId(clientCompany.getId());
        clientCompanyDTO.setLastInteractedBy(clientCompany.getLastInteractedBy());
        clientCompanyDTO.setLastInteractedWith(clientCompany.getLastInteractedWith());
        clientCompanyDTO.setLastInteractionDate(clientCompany.getLastInteractionDate());
        clientCompanyDTO.setMainPersonEmail(clientCompany.getMainPersonEmail());
        clientCompanyDTO.setMainPersonName(clientCompany.getMainPersonName());
        clientCompanyDTO.setMainPersonPhone(clientCompany.getMainPersonPhone());
        clientCompanyDTO.setClientTeams(clientCompany.getClientTeams());

        return clientCompanyDTO;
    }
}
