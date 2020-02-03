package com.consultant.model.mappers;

import com.consultant.model.dto.ClientDTO;
import com.consultant.model.dto.ClientTeamDTO;
import com.consultant.model.entities.Client;
import com.consultant.model.entities.ClientTeam;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AbstractClientMapper {
    AbstractClientMapper INSTANCE = Mappers.getMapper(AbstractClientMapper.class);

    ClientDTO clientToClientDTO(Client client);

    Client clientDTOToClient(ClientDTO clientDTO);

    ClientTeamDTO clientTeamToClientTeamDTO(ClientTeam clientTeam);

    ClientTeam clientTeamDTOToClientTeam(ClientTeamDTO clientTeamDTO);
}