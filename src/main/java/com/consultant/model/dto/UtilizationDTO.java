package com.consultant.model.dto;


import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UtilizationDTO {
    Long id;

    @CsvBindByName(column = "date")
    @CsvBindByPosition(position = 0)
    LocalDate date;

    @CsvBindByName(column = "ut")
    @CsvBindByPosition(position = 1)
    Double ut;

    @CsvBindByName(column = "aidedUt")
    @CsvBindByPosition(position = 2)
    Double aidedUt;

    @CsvBindByName(column = "threeMonthsUt")
    @CsvBindByPosition(position = 3)
    Double threeMonthsUt;

    @CsvBindByName(column = "threeMonthsAidedUt")
    @CsvBindByPosition(position = 4)
    Double threeMonthsAidedUt;
}
