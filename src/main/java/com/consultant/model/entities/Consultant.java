package com.consultant.model.entities;

import com.consultant.model.dto.ConsultantDTO;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    private LocalDate dateJoined;

    @OneToMany(mappedBy = "consultant")
    @JsonManagedReference(value="consultant-rating")
    private Set<TechnologyRating> ratings;

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
        existingConsultant.setDateJoined(consultantDTO.getDateJoined());

        return existingConsultant;
    }
}
