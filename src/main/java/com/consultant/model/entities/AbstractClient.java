package com.consultant.model.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@MappedSuperclass
abstract public class AbstractClient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    String name;

    @Column
    String mainPersonName;

    @Column
    String mainPersonEmail;

    @Column
    String mainPersonPhone;

    @Column
    LocalDate lastInteractionDate;

    @Column
    String lastInteractedWith;

    @Column
    String lastInteractedBy;
}
