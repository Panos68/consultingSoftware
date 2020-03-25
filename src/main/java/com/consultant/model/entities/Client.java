package com.consultant.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clients")
@NoArgsConstructor
@Setter
@Getter
public class Client extends AbstractClient {

    @OneToMany (cascade = CascadeType.ALL)
    @JoinColumn(name = "client.id")
    private List<ClientTeam> clientTeams = new ArrayList<>();
}
