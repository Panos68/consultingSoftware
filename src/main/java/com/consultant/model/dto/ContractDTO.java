package com.consultant.model.dto;

import com.consultant.model.converters.LocalDateConverter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvCustomBindByPosition;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class ContractDTO {
    private Long id;

    private Long consultantId;

    @CsvBindByName(column = "Price")
    @CsvBindByPosition(position = 7)
    private Integer price;

    @CsvBindByName(column = "Contract Started Date")
    @CsvCustomBindByPosition(position = 8, converter = LocalDateConverter.class)
    private LocalDate startedDate;

    @CsvBindByName(column = "Contract End Date")
    @CsvCustomBindByPosition(position = 9, converter = LocalDateConverter.class)
    private LocalDate endDate;

    @CsvBindByName(column = "Updated Contract Ending")
    @CsvCustomBindByPosition(position = 10, converter = LocalDateConverter.class)
    private LocalDate updatedContractEnding;

    @CsvBindByName(column = "Discount")
    @CsvBindByPosition(position = 11)
    private Integer discount;

    @CsvBindByName(column = "Signed")
    @CsvBindByPosition(position = 12)
    private Boolean signed;

    private LocalDate terminatedDate;

    private Long teamId;

    @JsonIgnoreProperties("ClientTeams")
    private ClientDTO client;

    private Boolean active;
}
