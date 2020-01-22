package com.consultant.model.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="candidates")
@Data
public class Candidate {

     public Candidate() {
    }

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @Column
    String linkedinUrl;

    @Column
    String role;

    @Column
    String company;

    @Column
    String location;

    @Column
    String consultant;

    @Column
    String diverse;

    @Column
    String source;

    @Column
    String comment;

}
