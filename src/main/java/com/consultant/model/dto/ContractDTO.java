package com.consultant.model.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ContractDTO {
    private Long consultantId;

    private Integer price;

    private LocalDate startedDate;

    private LocalDate endDate;

    private LocalDate updatedContractEnding;

    private Boolean signed;

    private LocalDate terminatedDate;

    private Long teamId;

    private Integer discount;

    private ClientDTO client;

    private Boolean active;
}
