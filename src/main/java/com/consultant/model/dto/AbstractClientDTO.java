package com.consultant.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public abstract class AbstractClientDTO {

    private Long id;

    private String name;

    private String mainPersonName;

    private String mainPersonEmail;

    private String mainPersonPhone;

    private LocalDate lastInteractionDate;

    private String lastInteractedWith;

    private String lastInteractedBy;
}
