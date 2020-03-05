package com.consultant.model.repositories;

import com.consultant.model.entities.Consultant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConsultantRepository extends JpaRepository<Consultant,Long> {
    Optional<Consultant> findByUserId (Long userId);
}
