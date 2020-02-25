package com.consultant.model.repositories;

import com.consultant.model.entities.Utilization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface UtilizationRepository extends JpaRepository<Utilization,Long> {
    Optional<Utilization> findByDate(LocalDate localDate);
}
