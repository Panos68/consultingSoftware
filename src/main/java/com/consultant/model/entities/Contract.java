package com.consultant.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@Table(name = "contracts")
@NoArgsConstructor
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer price;

    @Column
    private LocalDate startedDate;

    @Column
    private LocalDate endDate;

    @Column
    private LocalDate updatedContractEnding;

    @Column
    private Boolean signed;

    @ManyToOne
    @JoinColumn(name = "clientId")
    @JsonIgnoreProperties("clientTeams")
    private Client client;

    @Column
    private Boolean active;

    @Column
    private Integer discount;
}
