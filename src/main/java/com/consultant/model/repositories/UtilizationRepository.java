package com.consultant.model.repositories;

import com.consultant.model.entities.Utilization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtilizationRepository extends JpaRepository<Utilization,Long> {
}
