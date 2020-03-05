package com.consultant.model.dto;

import com.consultant.model.entities.Contract;
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
    private Long id;

    private String firstName;

    private String lastName;

    private List<Contract> contracts = new ArrayList<>();

    private Integer listPrice;

    private String other;

    private String teamName;

    private String clientName;

    private Long teamId;

    private ContractDTO activeContract;

    private LocalDate dateJoined;

    private Set<TechnologyRatingDTO> ratings = new HashSet<>();

    private Boolean deleted;

    private Long userId;
}
