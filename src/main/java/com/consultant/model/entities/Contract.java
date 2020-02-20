package com.consultant.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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

    @Column
    private String clientName;

    @Column
    private Boolean active;

    @Column
    private Integer discount;
}
