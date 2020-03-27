package com.consultant.model.services;

import com.consultant.model.dto.TechnologyDTO;
import com.consultant.model.entities.Technology;
import com.consultant.model.entities.TechnologyRating;
import com.consultant.model.enums.TechnologyType;
import com.consultant.model.repositories.TechnologyRatingRepository;
import com.consultant.model.repositories.TechnologyRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TechnologyServiceShould {

    private List<Technology> technologyList = new ArrayList<>();

    private List<TechnologyRating> technologyRatingList = new ArrayList<>();

    private Technology technology1 = new Technology();

    private Technology technology2 = new Technology();

    private TechnologyService technologyService;

    private TechnologyRating technologyRating = new TechnologyRating();

    private Long consultantId = 1L;

    @Mock
    private TechnologyRatingRepository technologyRatingRepository;

    @Mock
    private TechnologyRepository technologyRepository;

    @Before
    public void setUp() throws Exception {
        technologyService = new TechnologyService(technologyRepository, technologyRatingRepository);
        Mockito.when(technologyRepository.findAll()).thenReturn(technologyList);
    }

    @Test
    public void returnListOfAllExistingConsultants() {
        technologyList.add(technology1);
        technologyList.add(technology2);

        Set<TechnologyDTO> technologyDTOS = technologyService.getAll();
        Assert.assertThat(technologyDTOS.size(), is(2));
    }

    @Test
    public void returnEmptyListIfThereAreNoConsultants() {
        when(technologyRepository.findAll()).thenReturn(Collections.emptyList());
        Set<TechnologyDTO> technologyDTOS = technologyService.getAll();

        Assert.assertThat(technologyDTOS.isEmpty(), is(true));
    }

    @Test
    public void saveRatingOfTechnologyByGivenNameAndType() {
        String technologyName = "technologyName";
        technology1.setName(technologyName);
        technology1.setType(TechnologyType.BE);

        technologyService.saveRating(technology1, consultantId, technologyRating);

        Mockito.verify(technologyRatingRepository, Mockito.times(1)).saveAndFlush(technologyRating);
    }

    @Test
    public void addNewTechnologies() {
        technologyRating.setRating(1);
        Technology technology = new Technology();
        technology.setType(TechnologyType.BE);
        technology.setName("Java");
        technologyRating.setTechnology(technology);

        technologyService.updateRatings(Collections.singletonList(technologyRating), consultantId);

        Mockito.verify(technologyRatingRepository, Mockito.times(1)).saveAndFlush(technologyRating);
    }

    @Test
    public void updateExistingTechnologies() {
        TechnologyRating editedTechnology = createTechnology();
        editedTechnology.setRating(5);

        TechnologyRating existingTechnologyRating = createTechnology();
        technologyRatingList.add(existingTechnologyRating);

        Mockito.when(technologyRatingRepository.findByConsultantId(consultantId)).thenReturn(technologyRatingList);
        technologyService.updateRatings(Collections.singletonList(editedTechnology), consultantId);

        Mockito.verify(technologyRatingRepository, Mockito.times(1)).saveAndFlush(existingTechnologyRating);
    }

    private TechnologyRating createTechnology() {
        TechnologyRating technologyRating = new TechnologyRating();
        technologyRating.setRating(2);
        Technology technology = new Technology();
        technology.setType(TechnologyType.BE);
        technology.setName("Java");
        technologyRating.setTechnology(technology);

        return technologyRating;
    }
}