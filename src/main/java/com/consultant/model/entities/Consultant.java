package com.consultant.model.entities;

import com.consultant.model.dto.ConsultantDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "consultants")
@NoArgsConstructor
public class Consultant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "consultant.id")
    private List<Contract> contracts = new ArrayList<>();

    @Column
    private Integer listPrice;

    @Column
    private String other;

    @Column
    private String mainTechnologies;

    @Column
    private LocalDate dateJoined;

    @Transient
    private String teamName;

    @Transient
    private String clientName;

    @Transient
    private Long teamId;

    public static Consultant updateConsultant(Consultant existingConsultant, ConsultantDTO consultantDTO) {
        existingConsultant.setFirstName(consultantDTO.getFirstName());
        existingConsultant.setLastName(consultantDTO.getLastName());
        existingConsultant.setListPrice(consultantDTO.getListPrice());
        existingConsultant.setOther(consultantDTO.getOther());
        existingConsultant.setMainTechnologies(consultantDTO.getMainTechnologies());
        existingConsultant.setDateJoined(consultantDTO.getDateJoined());

        return existingConsultant;
    }
}
