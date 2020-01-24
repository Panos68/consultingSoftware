package com.consultant.model.converters.client;

import com.consultant.model.dto.ClientTeamDTO;
import com.consultant.model.entities.ClientTeam;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ClientTeamToDTO implements Converter<ClientTeam, ClientTeamDTO> {
    @Override
    public ClientTeamDTO convert(ClientTeam clientTeam) {
        ClientTeamDTO clientTeamDTO = new ClientTeamDTO();
        clientTeamDTO.setName(clientTeam.getName());
        clientTeamDTO.setId(clientTeam.getId());
        clientTeamDTO.setConsultants(clientTeam.getConsultants());
        clientTeamDTO.setLastInteractedBy(clientTeam.getLastInteractedBy());
        clientTeamDTO.setLastInteractedWith(clientTeam.getLastInteractedWith());
        clientTeamDTO.setLastInteractionDate(clientTeam.getLastInteractionDate());
        clientTeamDTO.setMainPersonEmail(clientTeam.getMainPersonEmail());
        clientTeamDTO.setMainPersonName(clientTeam.getMainPersonName());
        clientTeamDTO.setMainPersonPhone(clientTeam.getMainPersonPhone());
        clientTeamDTO.setMainTechnologies(clientTeam.getMainTechnologies());

        return clientTeamDTO;
    }
}
