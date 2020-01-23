package com.consultant.model.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ClientDTO {

    private Long id;

    private String name;

    private Integer consultantsAssigned;

    private String mainTechnologies;

    private String mainPersonName;

    private String mainPersonEmail;

    private String mainPersonPhone;

    private Date lastInteractionDate;

    private String lastInteractedWith;

    private String lastInteractedBy;
}
