package com.consultant.model.mappers;

import com.consultant.model.dto.CandidateDTO;
import com.consultant.model.entities.Candidate;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CandidateMapper {
    CandidateMapper INSTANCE = Mappers.getMapper( CandidateMapper.class );

    CandidateDTO candidateToCandidateDTO(Candidate candidate);

    Candidate candidateDTOToCandidate(CandidateDTO candidateDTO);
}