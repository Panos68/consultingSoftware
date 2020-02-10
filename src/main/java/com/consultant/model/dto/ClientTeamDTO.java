package com.consultant.model.dto;

import com.consultant.model.entities.Consultant;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ClientTeamDTO extends AbstractClientDTO {
    List<Consultant> consultants;

    String mainTechnologies;

    Long clientId;

    String clientName;
}
