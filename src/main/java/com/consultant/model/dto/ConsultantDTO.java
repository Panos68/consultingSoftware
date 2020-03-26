package com.consultant.model.dto;

import com.consultant.model.entities.Contract;
import com.consultant.model.converters.LocalDateConverter;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvCustomBindByPosition;
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
    @CsvBindByName(column = "firstName")
    @CsvBindByPosition(position = 0)
    private String firstName;

    @CsvBindByName(column = "lastName")
    @CsvBindByPosition(position = 1)
    private String lastName;

    @CsvBindByName(column = "clientName")
    @CsvBindByPosition(position = 2)
    private String clientName;

    @CsvBindByName(column = "teamName")
    @CsvBindByPosition(position = 3)
    private String teamName;

    @CsvBindByName(column = "listPrice")
    @CsvBindByPosition(position = 4)
    private Integer listPrice;

    @CsvBindByName(column = "dateJoined")
    @CsvCustomBindByPosition(position = 5, converter = LocalDateConverter.class)
    private LocalDate dateJoined;

    @CsvBindByName(column = "other")
    @CsvBindByPosition(position = 6)
    private String other;

    @CsvBindByName(column = "deleted")
    @CsvBindByPosition(position = 7)
    private Boolean deleted;

    private Long id;

    private List<Contract> contracts = new ArrayList<>();

    private Long teamId;

    private ContractDTO activeContract;

    private Set<TechnologyRatingDTO> ratings = new HashSet<>();

    private Long userId;
}
