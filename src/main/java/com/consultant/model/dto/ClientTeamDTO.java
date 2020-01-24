package com.consultant.model.dto;

import lombok.Data;

@Data
public class ClientTeamDTO extends AbstractClientDTO {
    Integer consultantsAssigned;

    String mainTechnologies;

    Long clientId;
}
