package com.consultant.model.mappers;

import com.consultant.model.dto.TechnologyDTO;
import com.consultant.model.dto.TechnologyRatingDTO;
import com.consultant.model.entities.Technology;
import com.consultant.model.entities.TechnologyRating;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TechnologyMapper {
    TechnologyMapper INSTANCE = Mappers.getMapper(TechnologyMapper.class);

    TechnologyDTO technologyToTechnologyDTO(Technology technology);

    TechnologyRatingDTO technologyRatingToTechnologyRatingDTO(TechnologyRating technologyRating);

    TechnologyRating technologyRatingDTOToTechnologyRating(TechnologyRatingDTO technologyRatingDTO);

}
