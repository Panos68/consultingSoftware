package com.consultant.model.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ConsultantDTO {
    Long id;

    String status;

    String lastName;

    String firstName;

    Integer listPrice;

    Integer price;

    Integer discount;

    Date contractStarted;

    Date contractEnding;

    Date updatedContractEnding;

    Boolean signed;

    String other;

    Long teamId;

    String teamName;

    String clientName;
}
