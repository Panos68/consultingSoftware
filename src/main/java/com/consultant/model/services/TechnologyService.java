package com.consultant.model.services;

import com.consultant.model.dto.TechnologyDTO;
import com.consultant.model.entities.Technology;
import com.consultant.model.entities.TechnologyRating;
import com.consultant.model.enums.Type;
import com.consultant.model.mappers.TechnologyMapper;
import com.consultant.model.repositories.TechnologyRatingRepository;
import com.consultant.model.repositories.TechnologyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TechnologyService {
    TechnologyRepository technologyRepository;
    TechnologyRatingRepository technologyRatingRepository;

    @Autowired
    public TechnologyService(TechnologyRepository technologyRepository, TechnologyRatingRepository technologyRatingRepository) {
        this.technologyRepository = technologyRepository;
        this.technologyRatingRepository = technologyRatingRepository;
    }

    public Set<TechnologyDTO> getAll() {
        List<Technology> technologyList = technologyRepository.findAll();
        Set<TechnologyDTO> technologyDTOS = new HashSet<>();
        technologyList.forEach(technology -> {
            TechnologyDTO technologyDTO = TechnologyMapper.INSTANCE.technologyToTechnologyDTO(technology);

            technologyDTOS.add(technologyDTO);
        });

        return technologyDTOS;
    }

    public Technology getTechnologyByNameAndType(String name, Type type) {
        Optional<Technology> optionalTechnology = technologyRepository.findByNameAndType(name, type);
        return optionalTechnology.orElseGet(() -> createNewTechnology(name, type));
    }

    private Technology createNewTechnology(String name, Type type) {
        Technology technology = new Technology();
        technology.setName(name);
        technology.setType(type);
        technologyRepository.saveAndFlush(technology);
        return technology;
    }

    public void saveRating(Technology technology, Long consultantId, TechnologyRating rating) {
        Technology savedTechnology = getTechnologyByNameAndType(technology.getName(), technology.getType());
        rating.setTechnology(savedTechnology);
        rating.setId(new TechnologyRating.TechnologyRatingKey(consultantId, savedTechnology.getId()));
        technologyRatingRepository.saveAndFlush(rating);
    }

    public void updateRatings(Set<TechnologyRating> newRatings, Long consultantId) {
        List<TechnologyRating> existingRatings = technologyRatingRepository.findByConsultantId(consultantId);

        List<TechnologyRating> ratingsToUpdate = existingRatings.stream()
                .filter(rating -> (newRatings.stream().map(r -> r.getTechnology().getName())).collect(Collectors.toList())
                        .contains(rating.getTechnology().getName()))
                .collect(Collectors.toList());

        existingRatings.removeAll(ratingsToUpdate);
        existingRatings.forEach(rating -> technologyRatingRepository.delete(rating));

        newRatings.forEach(rating ->
        {
            Optional<TechnologyRating> existingRating = ratingsToUpdate.stream().filter(r -> r.getTechnology().getName().equals(rating.getTechnology().getName())
                    && r.getTechnology().getType().equals(rating.getTechnology().getType())).findFirst();
            if (existingRating.isPresent()) {
                existingRating.get().setRating(rating.getRating());
                technologyRatingRepository.saveAndFlush(existingRating.get());
            } else {
                saveRating(rating.getTechnology(), consultantId, rating);
            }
        });
    }
}
