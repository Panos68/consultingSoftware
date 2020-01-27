package com.consultant.model.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "client_teams")
@NoArgsConstructor
@Data
public class ClientTeam extends AbstractClient {

    @Column
    private String mainTechnologies;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "team.id")
    private List<Consultant> consultants = new ArrayList<>();

}
