package com.consultant.model.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class VacationDTO {
    private Long id;

    private String description;

    private LocalDate startingDate;

    private LocalDate endDate;

    private String email;

    private Long userId;

    private Boolean isLongTerm;
}
