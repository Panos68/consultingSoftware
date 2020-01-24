package com.consultant.model.dto;

import com.consultant.model.entities.Consultant;
import lombok.Data;

import java.util.List;

@Data
public class ClientTeamDTO extends AbstractClientDTO {
    List<Consultant> consultants;

    String mainTechnologies;

    Long clientId;
}
