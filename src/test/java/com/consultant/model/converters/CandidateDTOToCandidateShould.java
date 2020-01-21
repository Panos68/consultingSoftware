package com.consultant.model.converters;

import com.consultant.model.Candidate;
import com.consultant.model.dto.CandidateDTO;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class CandidateDTOToCandidateShould {

    private CandidateDTOToCandidate candidateDTOToCandidate = new CandidateDTOToCandidate();

    @Test
    public void keepLinkedinUrlAfterConversion(){
        CandidateDTO candidateDTO = new CandidateDTO();
        String linkedinUrl = "linkedinUrl";
        candidateDTO.setLinkedinUrl(linkedinUrl);
        Candidate candidate = this.candidateDTOToCandidate.convert(candidateDTO);

        assertEquals(candidate.getLinkedinUrl(),linkedinUrl);
    }

    @Test
    public void keepSourceAfterConversion(){
        CandidateDTO candidateDTO = new CandidateDTO();
        String source = "source";
        candidateDTO.setSource(source);
        Candidate candidate = this.candidateDTOToCandidate.convert(candidateDTO);

        assertEquals(candidate.getSource(),source);
    }

    @Test
    public void keepLocationAfterConversion(){
        CandidateDTO candidateDTO = new CandidateDTO();
        String location = "location";
        candidateDTO.setLocation(location);
        Candidate candidate = this.candidateDTOToCandidate.convert(candidateDTO);

        assertEquals(candidate.getLocation(),location);
    }

    @Test
    public void keepDiverseAfterConversion(){
        CandidateDTO candidateDTO = new CandidateDTO();
        String diverese = "diverese";
        candidateDTO.setDiverse(diverese);
        Candidate candidate = this.candidateDTOToCandidate.convert(candidateDTO);

        assertEquals(candidate.getDiverse(),diverese);
    }

    @Test
    public void keepCompanyAfterConversion(){
        CandidateDTO candidateDTO = new CandidateDTO();
        String company = "company";
        candidateDTO.setCompany(company);
        Candidate candidate = this.candidateDTOToCandidate.convert(candidateDTO);

        assertEquals(candidate.getCompany(),company);
    }

    @Test
    public void keepRoleAfterConversion(){
        CandidateDTO candidateDTO = new CandidateDTO();
        String role = "role";
        candidateDTO.setRole(role);
        Candidate candidate = this.candidateDTOToCandidate.convert(candidateDTO);

        assertEquals(candidate.getRole(),role);
    }

    @Test
    public void keepCommentAfterConversion(){
        CandidateDTO candidateDTO = new CandidateDTO();
        String comment = "comment";
        candidateDTO.setComment(comment);
        Candidate authcandidater = this.candidateDTOToCandidate.convert(candidateDTO);

        assertEquals(authcandidater.getComment(),comment);
    }

    @Test
    public void keepIdAfterConversion(){
        CandidateDTO candidateDTO = new CandidateDTO();
        Long id = 1L;
        candidateDTO.setId(id);
        Candidate candidate = this.candidateDTOToCandidate.convert(candidateDTO);

        assertEquals(candidate.getId(),id);
    }

    @Test
    public void keepConsultantAfterConversion(){
        CandidateDTO candidateDTO = new CandidateDTO();
        String consultant = "consultant";
        candidateDTO.setConsultant(consultant);
        Candidate candidate = this.candidateDTOToCandidate.convert(candidateDTO);

        assertEquals(candidate.getConsultant(),consultant);
    }


}



