package com.consultant.model.repositories;

import com.consultant.model.entities.Technology;
import com.consultant.model.enums.TechnologyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TechnologyRepository extends JpaRepository<Technology,Long> {
    Optional<Technology> findByNameAndType(String name, TechnologyType type);
}
