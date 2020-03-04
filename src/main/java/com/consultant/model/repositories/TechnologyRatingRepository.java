package com.consultant.model.repositories;

import com.consultant.model.entities.TechnologyRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TechnologyRatingRepository extends JpaRepository<TechnologyRating,Long> {
    List<TechnologyRating> findByConsultantId (long consultantId);
}