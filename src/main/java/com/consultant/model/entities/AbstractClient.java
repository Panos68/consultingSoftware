package com.consultant.model.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@MappedSuperclass
abstract public class AbstractClient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String mainPersonName;

    @Column
    private String mainPersonEmail;

    @Column
    private String mainPersonPhone;

    @Column
    private LocalDate lastInteractionDate;

    @Column
    private String lastInteractedWith;

    @Column
    private String lastInteractedBy;

    @Column
    private Boolean deleted;
}
