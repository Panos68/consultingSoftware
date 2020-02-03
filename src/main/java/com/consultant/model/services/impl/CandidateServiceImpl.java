package com.consultant.model.services.impl;

import com.consultant.model.mappers.CandidateMapper;
import com.consultant.model.entities.Candidate;
import com.consultant.model.dto.CandidateDTO;
import com.consultant.model.exception.EntityAlreadyExists;
import com.consultant.model.exception.NoMatchException;
import com.consultant.model.repositories.CandidateRepository;
import com.consultant.model.services.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CandidateServiceImpl implements CandidateService {

    CandidateRepository candidateRepository;


    @Autowired
    public CandidateServiceImpl(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    @Override
    public Set<CandidateDTO> getAllCandidates() {
        List<Candidate> candidateList = candidateRepository.findAll();
        Set<CandidateDTO> candidateDTOS = new HashSet<>();
        candidateList.forEach(candidate -> {
            CandidateDTO candidateDTO = CandidateMapper.INSTANCE.candidateToCandidateDTO( candidate );

            candidateDTOS.add(candidateDTO);
        });

        return candidateDTOS;
    }

    @Override
    public void createCandidate(CandidateDTO candidateDTO) throws Exception {
        Optional<Candidate> existingCandidate = candidateRepository.findByLinkedinUrl(candidateDTO.getLinkedinUrl());
        if (existingCandidate.isPresent()) {
            throw new EntityAlreadyExists("Candidate already exists");
        }
        Candidate candidate = CandidateMapper.INSTANCE.candidateDTOToCandidate( candidateDTO );

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
            throw new NoMatchException("The id provided doesn't match any candidate");
        }

        return existingCandidate;
    }
}
