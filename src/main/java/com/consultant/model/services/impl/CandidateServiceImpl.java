package com.consultant.model.services.impl;

import com.consultant.model.Candidate;
import com.consultant.model.dto.CandidateDTO;
import com.consultant.model.exception.CandidateAlreadyExistsException;
import com.consultant.model.repositories.CandidateRepository;
import com.consultant.model.services.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CandidateServiceImpl implements CandidateService {

    CandidateRepository candidateRepository;

    ConversionService conversionService;

    @Autowired
    public CandidateServiceImpl(CandidateRepository candidateRepository, ConversionService conversionService) {
        this.candidateRepository = candidateRepository;
        this.conversionService = conversionService;
    }

    @Override
    public Set<CandidateDTO> getAllCandidates() {
        List<Candidate> candidateList = candidateRepository.findAll();
        Set<CandidateDTO> candidateDTOS = new HashSet<>();
        candidateList.forEach(candidate -> {
            final CandidateDTO candidateDTO = conversionService.convert(candidate, CandidateDTO.class);
            candidateDTOS.add(candidateDTO);
        });

        return candidateDTOS;
    }

    @Override
    public void createCandidate(CandidateDTO candidateDTO) throws Exception {
        Optional<Candidate> existingCandidate = candidateRepository.findByLinkedinUrl(candidateDTO.getLinkedinUrl());
        if (existingCandidate.isPresent()) {
            throw new CandidateAlreadyExistsException("Candidate already exists");
        }

        final Candidate candidate = conversionService.convert(candidateDTO, Candidate.class);

        candidateRepository.saveAndFlush(candidate);
    }

    @Override
    public void editCandidate(CandidateDTO candidateDTO) throws Exception {
        Optional<Candidate> existingCandidate = getExistingCandidateById(candidateDTO.getId());

        Candidate updatedCandidate = updateCandidate(existingCandidate.get(), candidateDTO);

        candidateRepository.saveAndFlush(updatedCandidate);
    }

    @Override
    public void deleteCandidate(Long id) throws Exception {
        Optional<Candidate> existingCandidate = getExistingCandidateById(id);

        candidateRepository.delete(existingCandidate.get());
    }

    private Candidate updateCandidate(Candidate existingCandidate, CandidateDTO candidateDTO) {
        existingCandidate.setComment(candidateDTO.getComment());
        existingCandidate.setConsultant(candidateDTO.getConsultant());
        existingCandidate.setRole(candidateDTO.getRole());
        existingCandidate.setCompany(candidateDTO.getCompany());
        existingCandidate.setDiverse(candidateDTO.getDiverse());
        existingCandidate.setLinkedinUrl(candidateDTO.getLinkedinUrl());
        existingCandidate.setLocation(candidateDTO.getLocation());
        existingCandidate.setSource(candidateDTO.getSource());
        return existingCandidate;
    }

    private Optional<Candidate> getExistingCandidateById(Long id) throws Exception {
        Optional<Candidate> existingCandidate = candidateRepository.findById(id);
        if (!existingCandidate.isPresent()) {
            throw new Exception("The id provided doesn't match any candidate");
        }

        return existingCandidate;
    }
}
