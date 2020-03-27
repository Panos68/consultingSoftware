package com.consultant.model.dto;

import com.consultant.model.converters.LocalDateConverter;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvCustomBindByPosition;
import com.opencsv.bean.CsvRecurse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
public class ConsultantDTO {
    @CsvBindByName(column = "FirstName")
    @CsvBindByPosition(position = 0)
    private String firstName;

    @CsvBindByName(column = "LastName")
    @CsvBindByPosition(position = 1)
    private String lastName;

    @CsvBindByName(column = "ClientName")
    @CsvBindByPosition(position = 2)
    private String clientName;

    @CsvBindByName(column = "TeamName")
    @CsvBindByPosition(position = 3)
    private String teamName;

    @CsvBindByName(column = "ListPrice")
    @CsvBindByPosition(position = 4)
    private Integer listPrice;

    @CsvBindByName(column = "Date Joined")
    @CsvCustomBindByPosition(position = 5, converter = LocalDateConverter.class)
    private LocalDate dateJoined;

    @CsvBindByName(column = "Other")
    @CsvBindByPosition(position = 6)
    private String other;

    @CsvRecurse
    private ContractDTO activeContract;

    private Long id;

    private Boolean deleted;

    private List<ContractDTO> contracts = new ArrayList<>();

    private Long teamId;

    private Set<TechnologyRatingDTO> ratings = new HashSet<>();

    private Long userId;
}
