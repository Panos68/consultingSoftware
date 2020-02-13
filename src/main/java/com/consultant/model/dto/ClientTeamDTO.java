package com.consultant.model.dto;

import com.consultant.model.entities.Consultant;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ClientTeamDTO extends AbstractClientDTO {
    private List<Consultant> consultants;

    private String mainTechnologies;

    private Long clientId;

    private String clientName;
}
