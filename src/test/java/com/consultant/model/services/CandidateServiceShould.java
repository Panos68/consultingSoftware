package com.consultant.model.services;

import com.consultant.model.entities.Candidate;
import com.consultant.model.dto.CandidateDTO;
import com.consultant.model.exception.EntityAlreadyExists;
import com.consultant.model.exception.NoMatchException;
import com.consultant.model.repositories.CandidateRepository;
import com.consultant.model.services.impl.CandidateService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

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

    private CandidateService candidateService;

    private Long candidateId = 1L;

    private Candidate candidate1 = new Candidate();

    private Candidate candidate2 = new Candidate();

    private List<Candidate> candidateList = new ArrayList<>();

    private CandidateDTO candidateDTO = new CandidateDTO();

    @Before
    public void setUp() {
        candidateService = new CandidateService(candidateRepository);

        candidate1 = new Candidate();
        candidate1.setId(candidateId);
        when(candidateRepository.findAll()).thenReturn(candidateList);

        candidateDTO = new CandidateDTO();
    }

    @Test
    public void returnListOfAllExistingCandidates() throws Exception {
        candidateList.add(candidate1);
        candidateList.add(candidate2);
        Set<CandidateDTO> candidateDTOS = candidateService.getAll();
        Assert.assertThat(candidateDTOS.size(), is(2));
    }

    @Test
    public void returnEmptyListIfThereAreNoCandidates() throws Exception {
        when(candidateRepository.findAll()).thenReturn(candidateList);
        Set<CandidateDTO> candidateDTOS = candidateService.getAll();

        Assert.assertThat(candidateDTOS.isEmpty(), is(true));
    }

    @Test
    public void saveToRepositoryWhenCreatingCandidate() throws Exception {
        candidateDTO = new CandidateDTO();
        candidateDTO.setId(candidateId);
        Mockito.when(candidateRepository.saveAndFlush(Mockito.any())).thenReturn(candidate1);
        candidateService.create(candidateDTO);

        verify(candidateRepository, times(1)).saveAndFlush(candidate1);
    }

    @Test(expected = EntityAlreadyExists.class)
    public void throwExceptionWhenCreatingCandidateWithSameLinkedIn() throws Exception {
        String linkedInUrl = "linkedinUrl";
        candidate1.setLinkedinUrl(linkedInUrl);
        candidateDTO.setLinkedinUrl(linkedInUrl);
        Mockito.when(candidateRepository.findByLinkedinUrl(linkedInUrl)).thenReturn(Optional.ofNullable(candidate1));
        candidateService.create(candidateDTO);
    }

    @Test
    public void deleteInRepositoryWhenDeletingExistingCandidate() throws Exception {
        Mockito.when(candidateRepository.findById(candidateId)).thenReturn(Optional.ofNullable(candidate1));
        candidateService.delete(candidateId);

        verify(candidateRepository, times(1)).delete(candidate1);
    }

    @Test(expected = NoMatchException.class)
    public void throwExceptionWhenDeletingNonExistingCandidate() throws Exception {
        candidateService.delete(candidateId);
    }

    @Test
    public void updateOnEditingExistingCandidate() throws Exception {
        candidateDTO.setComment("updated");
        candidateDTO.setId(candidateId);
        Mockito.when(candidateRepository.findById(candidateId)).thenReturn(Optional.ofNullable(candidate1));
        candidateService.edit(candidateDTO);

        verify(candidateRepository, times(1)).saveAndFlush(candidate1);
    }

    @Test(expected = NoMatchException.class)
    public void throwExceptionWhenUpdatingNonExistingCandidate() throws Exception {
        candidateDTO.setComment("updated");
        candidateDTO.setId(candidateId);
        Mockito.when(candidateRepository.findById(candidateId)).thenReturn(Optional.empty());
        candidateService.edit(candidateDTO);
    }

}
