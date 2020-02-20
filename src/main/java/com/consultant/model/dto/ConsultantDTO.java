package com.consultant.model.dto;

import com.consultant.model.entities.Contract;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class ConsultantDTO {
    private Long id;

    private String firstName;

    private String lastName;

    private List<Contract> contracts = new ArrayList<>();

    private Integer listPrice;

    private String other;

    private String mainTechnologies;

    private String teamName;

    private String clientName;

    private Long teamId;

    private ContractDTO activeContract;
}
