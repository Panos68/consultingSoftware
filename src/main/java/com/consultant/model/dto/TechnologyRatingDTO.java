package com.consultant.model.dto;

import com.consultant.model.entities.TechnologyRating;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TechnologyRatingDTO {
    private TechnologyRating.TechnologyRatingKey id;

    private TechnologyDTO technology;

    private int rating;
}
