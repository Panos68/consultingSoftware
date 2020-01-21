package com.consultant.model.services;

import com.consultant.model.Candidate;
import com.consultant.model.dto.CandidateDTO;
import com.consultant.model.exception.CandidateAlreadyExistsException;
import com.consultant.model.exception.NoMatchException;
import com.consultant.model.repositories.CandidateRepository;
import com.consultant.model.services.impl.CandidateServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.convert.ConversionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CandidateServiceShould {

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private ConversionService conversionService;

    private CandidateService candidateService;

    private Long candidateId = 1L;

    private Candidate candidate1 = new Candidate();

    private Candidate candidate2 = new Candidate();

    private List<Candidate> candidateList = new ArrayList<>();

    private CandidateDTO candidateDTO = new CandidateDTO();

    @Before
    public void setUp() {
        candidateService = new CandidateServiceImpl(candidateRepository, conversionService);

        candidate1 = new Candidate();
        candidate1.setId(candidateId);
        when(candidateRepository.findAll()).thenReturn(candidateList);

        candidateDTO = new CandidateDTO();
        when(conversionService.convert(candidateDTO, Candidate.class)).thenReturn(candidate1);
        when(conversionService.convert(candidate1, CandidateDTO.class)).thenReturn(candidateDTO);

    }

    @Test
    public void returnListOfAllExistingCandidates() throws Exception {
        candidateList.add(candidate1);
        candidateList.add(candidate2);
        Set<CandidateDTO> authorsDTO = candidateService.getAllCandidates();
        Assert.assertThat(authorsDTO.size(), is(2));
    }

    @Test
    public void returnEmptyListIfThereAreNoAuthors() throws Exception {
        when(candidateRepository.findAll()).thenReturn(candidateList);
        Set<CandidateDTO> authorsDTO = candidateService.getAllCandidates();

        Assert.assertThat(authorsDTO.isEmpty(), is(true));
    }

    @Test
    public void saveToRepositoryWhenCreatingCandidate() throws Exception {
        candidateService.createCandidate(candidateDTO);

        verify(candidateRepository, times(1)).saveAndFlush(candidate1);
    }

    @Test(expected = CandidateAlreadyExistsException.class)
    public void throwExceptionWhenCreatingCandidateWithSameLinkedIn() throws Exception {
        String linkedInUrl = "linkedinUrl";
        candidate1.setLinkedinUrl(linkedInUrl);
        candidateDTO.setLinkedinUrl(linkedInUrl);
        Mockito.when(candidateRepository.findByLinkedinUrl(linkedInUrl)).thenReturn(Optional.ofNullable(candidate1));
        candidateService.createCandidate(candidateDTO);
    }

    @Test
    public void deleteInRepositoryWhenDeletingExistingCandidate() throws Exception {
        Mockito.when(candidateRepository.findById(candidateId)).thenReturn(Optional.ofNullable(candidate1));
        candidateService.deleteCandidate(candidateId);

        verify(candidateRepository, times(1)).delete(candidate1);
    }

    @Test(expected = NoMatchException.class)
    public void throwExceptionWhenDeletingNonExistingCandidate() throws Exception {
        candidateService.deleteCandidate(candidateId);
    }

    @Test
    public void updateOnEditingExistingCandidate() throws Exception {
        candidateDTO.setComment("updated");
        candidateDTO.setId(candidateId);
        Mockito.when(candidateRepository.findById(candidateId)).thenReturn(Optional.ofNullable(candidate1));
        candidateService.editCandidate(candidateDTO);

        verify(candidateRepository, times(1)).saveAndFlush(candidate1);
    }

    @Test(expected = NoMatchException.class)
    public void throwExceptionWhenUpdatingNonExistingCandidate() throws Exception {
        candidateDTO.setComment("updated");
        candidateDTO.setId(candidateId);
        Mockito.when(candidateRepository.findById(candidateId)).thenReturn(Optional.empty());
        candidateService.editCandidate(candidateDTO);

        verify(candidateRepository, times(1)).saveAndFlush(candidate1);
    }

}
