package com.consultant.model.dto;

import com.consultant.model.entities.ClientTeam;
import lombok.Data;

import java.util.List;

@Data
public class ClientCompanyDTO extends AbstractClientDTO{
    private List<ClientTeam> clientTeams;
}
