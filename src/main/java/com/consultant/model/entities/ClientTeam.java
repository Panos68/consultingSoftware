package com.consultant.model.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "client_teams")
@NoArgsConstructor
@Data
public class ClientTeam extends AbstractClient {
    @Column
    Integer consultantsAssigned;

    @Column
    String mainTechnologies;
}
