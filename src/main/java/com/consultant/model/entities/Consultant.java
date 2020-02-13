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
    private String status;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private Integer price;

    @Column
    private Integer listPrice;

    @Column
    private Integer discount;

    @Column
    private LocalDate contractStarted;

    @Column
    private LocalDate contractEnding;

    @Column
    private LocalDate updatedContractEnding;

    @Column
    private Boolean signed;

    @Column
    private String other;

    @Column
    private String mainTechnologies;

    @Transient
    private String teamName;

    @Transient
    private String clientName;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "consultant.id")
    private List<HistoricalData> historicalDataList = new ArrayList<>();

    public static Consultant updateConsultant(Consultant existingConsultant, ConsultantDTO consultantDTO) {
        existingConsultant.setFirstName(consultantDTO.getFirstName());
        existingConsultant.setLastName(consultantDTO.getLastName());
        existingConsultant.setListPrice(consultantDTO.getListPrice());
        existingConsultant.setDiscount(consultantDTO.getDiscount());
        existingConsultant.setOther(consultantDTO.getOther());
        existingConsultant.setMainTechnologies(consultantDTO.getMainTechnologies());
        existingConsultant.setContractEnding(consultantDTO.getContractEnding());
        existingConsultant.setContractStarted(consultantDTO.getContractStarted());
        existingConsultant.setUpdatedContractEnding(consultantDTO.getUpdatedContractEnding());
        existingConsultant.setPrice(consultantDTO.getPrice());
        existingConsultant.setSigned(consultantDTO.getSigned());
        existingConsultant.setStatus(consultantDTO.getStatus());

        return existingConsultant;
    }
}
