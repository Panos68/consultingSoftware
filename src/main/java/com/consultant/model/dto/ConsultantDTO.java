package com.consultant.model.dto;

import lombok.Data;

import java.time.LocalDate;


@Data
public class ConsultantDTO {
    Long id;

    String status;

    String lastName;

    String firstName;

    Integer listPrice;

    Integer price;

    Integer discount;

    LocalDate contractStarted;

    LocalDate contractEnding;

    LocalDate updatedContractEnding;

    Boolean signed;

    String other;

    Long teamId;

    String teamName;

    String clientName;
}
