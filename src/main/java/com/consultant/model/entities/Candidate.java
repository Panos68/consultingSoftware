package com.consultant.model.entities;

import lombok.Data;

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
    private String linkedinUrl;

    @Column
    private String role;

    @Column
    private String company;

    @Column
    private String location;

    @Column
    private String consultant;

    @Column
    private String diverse;

    @Column
    private String source;

    @Column
    private String comment;

}
