package com.consultant.model.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clients")
@NoArgsConstructor
@Data
public class Client extends AbstractClient {

    @OneToMany (cascade = CascadeType.ALL)
    @JoinColumn(name = "client.id")
    private List<ClientTeam> clientTeams = new ArrayList<>();
}
