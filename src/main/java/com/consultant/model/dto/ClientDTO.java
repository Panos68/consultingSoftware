package com.consultant.model.dto;

import com.consultant.model.entities.ClientTeam;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ClientDTO extends AbstractClientDTO{
    private List<ClientTeam> clientTeams;
}
