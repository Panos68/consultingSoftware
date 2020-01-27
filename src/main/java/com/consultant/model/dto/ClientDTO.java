package com.consultant.model.dto;

import com.consultant.model.entities.ClientTeam;
import lombok.Data;

import java.util.List;

@Data
public class ClientDTO extends AbstractClientDTO{
    private List<ClientTeam> clientTeams;
}
