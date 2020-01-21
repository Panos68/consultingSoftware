package com.consultant.model.converters;

import com.consultant.model.Candidate;
import com.consultant.model.dto.CandidateDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CandidateToCandidateDTO implements Converter<Candidate, CandidateDTO> {

    @Override
    public CandidateDTO convert(Candidate candidate) {
         CandidateDTO candidateDTO = new CandidateDTO();
         candidateDTO.setComment(candidate.getComment());
         candidateDTO.setConsultant(candidate.getConsultant());
         candidateDTO.setRole(candidate.getRole());
         candidateDTO.setCompany(candidate.getCompany());
         candidateDTO.setDiverse(candidate.getDiverse());
         candidateDTO.setLinkedinUrl(candidate.getLinkedinUrl());
         candidateDTO.setLocation(candidate.getLocation());
         candidateDTO.setSource(candidate.getSource());
         candidateDTO.setId(candidate.getId());
        return candidateDTO;
    }
}
