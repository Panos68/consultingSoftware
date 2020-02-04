package com.consultant.model.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Table(name="vacations")
@NoArgsConstructor
public class Vacation {

    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column
    private String description;

    @Column
    private LocalDate startingDate;

    @Column
    private LocalDate endDate;

    @Transient
    private String userName;

    @Transient
    private Long userId;

}
