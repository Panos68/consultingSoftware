package com.consultant.model.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "clients")
@Data
@NoArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    String name;

    @Column
    Integer consultantsAssigned;

    @Column
    String mainTechnologies;

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
