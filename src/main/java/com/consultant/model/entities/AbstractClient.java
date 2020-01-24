package com.consultant.model.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

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
    Date lastInteractionDate;

    @Column
    String lastInteractedWith;

    @Column
    String lastInteractedBy;
}
