package com.consultant.model.services;

import com.consultant.model.dto.CandidateDTO;

import java.util.Set;

public interface CandidateService {
    Set<CandidateDTO> getAllCandidates();

    void createCandidate(CandidateDTO candidateDTO) throws Exception;

    void editCandidate(CandidateDTO candidateDTO) throws Exception;

    void deleteCandidate(Long id) throws Exception;
}
