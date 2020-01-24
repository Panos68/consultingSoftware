package com.consultant.model.converters.client;

import com.consultant.model.dto.ClientTeamDTO;
import com.consultant.model.entities.ClientTeam;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DTOtoClientTeam implements Converter<ClientTeamDTO, ClientTeam> {

    public ClientTeam convert(ClientTeamDTO clientTeamDTO) {
        ClientTeam clientTeam = new ClientTeam();
        clientTeam.setName(clientTeamDTO.getName());
        clientTeam.setId(clientTeamDTO.getId());
        clientTeam.setConsultantsAssigned(clientTeamDTO.getConsultantsAssigned());
        clientTeam.setLastInteractedBy(clientTeamDTO.getLastInteractedBy());
        clientTeam.setLastInteractedWith(clientTeamDTO.getLastInteractedWith());
        clientTeam.setLastInteractionDate(clientTeamDTO.getLastInteractionDate());
        clientTeam.setMainPersonEmail(clientTeamDTO.getMainPersonEmail());
        clientTeam.setMainPersonName(clientTeamDTO.getMainPersonName());
        clientTeam.setMainPersonPhone(clientTeamDTO.getMainPersonPhone());
        clientTeam.setMainTechnologies(clientTeamDTO.getMainTechnologies());
        return clientTeam;
    }
}
