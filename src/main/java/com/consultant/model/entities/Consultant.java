package com.consultant.model.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "consultants")
@NoArgsConstructor
public class Consultant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    String status;

    @Column
    String firstName;

    @Column
    String lastName;

    @Column
    Integer price;

    @Column
    Integer listPrice;

    @Column
    Integer discount;

    @Column
    Date contractStarted;

    @Column
    Date contractEnding;

    @Column
    Date updatedContractEnding;

    @Column
    Boolean signed;

    @Column
    String other;
}
