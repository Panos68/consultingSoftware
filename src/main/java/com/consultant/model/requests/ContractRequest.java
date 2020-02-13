package com.consultant.model.requests;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ContractRequest {
    private Long consultantId;

    private String status;

    private Integer listPrice;

    private Integer price;

    private Integer discount;

    private LocalDate contractStarted;

    private LocalDate contractEnding;

    private LocalDate updatedContractEnding;

    private Boolean signed;

    private LocalDate terminatedDate;

    private Long teamId;
}
