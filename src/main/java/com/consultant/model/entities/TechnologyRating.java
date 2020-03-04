package com.consultant.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Setter
@Getter
@Table(name = "consultant_technologies")
@NoArgsConstructor
public class TechnologyRating {
    @EmbeddedId
    private TechnologyRatingKey id;

    @ManyToOne
    @MapsId("consultant_id")
    @JoinColumn(name = "consultant_id")
    @JsonBackReference(value="consultant-rating")
    private Consultant consultant;

    @ManyToOne
    @MapsId("technology_id")
    @JoinColumn(name = "technology_id")
    @JsonBackReference(value="technology-rating")
    private Technology technology;

    private int rating;

    @Embeddable
    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TechnologyRatingKey implements Serializable {

        @Column(name = "consultant_id")
        Long consultantId;

        @Column(name = "technology_id")
        Long technologyId;
    }
}
