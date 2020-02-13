package com.consultant.model.dto;

import com.consultant.model.entities.HistoricalData;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class ConsultantDTO {
    private Long id;

    private String status;

    private String lastName;

    private String firstName;

    private Integer listPrice;

    private Integer price;

    private Integer discount;

    private LocalDate contractStarted;

    private LocalDate contractEnding;

    private LocalDate updatedContractEnding;

    private Boolean signed;

    private String other;

    private Long teamId;

    private String teamName;

    private String clientName;

    private String mainTechnologies;

    private List<HistoricalData> historicalDataList = new ArrayList<>();
}
