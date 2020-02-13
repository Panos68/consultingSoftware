package com.consultant.model.repositories;

import com.consultant.model.entities.HistoricalData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoricalDataRepository extends JpaRepository<HistoricalData,Long> {
}
