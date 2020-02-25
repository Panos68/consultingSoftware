package com.consultant.model.dto;


import lombok.Data;

import java.time.LocalDate;

@Data
public class UtilizationDTO {
    Long id;

    LocalDate date;

    Double ut;

    Double aidedUt;

    Double threeMonthsUt;

    Double threeMonthsAidedUt;
}
