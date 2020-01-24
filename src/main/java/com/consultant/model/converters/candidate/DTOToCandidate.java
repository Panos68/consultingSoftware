package com.consultant.model.converters.candidate;

import com.consultant.model.entities.Candidate;
import com.consultant.model.dto.CandidateDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DTOToCandidate implements Converter<CandidateDTO, Candidate> {

    @Override
    public Candidate convert(CandidateDTO candidateDTO) {
        Candidate candidate = new Candidate();
        candidate.setComment(candidateDTO.getComment());
        candidate.setConsultant(candidateDTO.getConsultant());
        candidate.setRole(candidateDTO.getRole());
        candidate.setCompany(candidateDTO.getCompany());
        candidate.setDiverse(candidateDTO.getDiverse());
        candidate.setLinkedinUrl(candidateDTO.getLinkedinUrl());
        candidate.setLocation(candidateDTO.getLocation());
        candidate.setSource(candidateDTO.getSource());
        Long candidateDTOId = candidateDTO.getId();
        if (candidateDTOId != null) {
            candidate.setId(candidateDTOId);
        }

        return candidate;
    }
}
