package com.consultant.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@Table(name = "utilization")
@NoArgsConstructor
public class Utilization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    LocalDate date;

    @Column
    Double ut;

    @Column
    Double aidedUt;

    @Column
    Double threeMonthsUt;

    @Column
    Double threeMonthsAidedUt;
}
