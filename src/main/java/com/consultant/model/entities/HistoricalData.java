package com.consultant.model.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "historical_data")
@NoArgsConstructor
public class HistoricalData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String clientName;

    @Column
    private LocalDate startedDate;

    @Column
    private LocalDate endDate;

    public HistoricalData(String clientName, LocalDate startedDate, LocalDate endDate) {
        this.clientName = clientName;
        this.startedDate = startedDate;
        this.endDate = endDate;
    }
}
