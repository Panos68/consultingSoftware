package com.consultant.model.converters;

import com.consultant.model.Candidate;
import com.consultant.model.dto.CandidateDTO;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CandidateToCandidateDTOTest {

    private CandidateToCandidateDTO candidateToCandidateDTO = new CandidateToCandidateDTO();

    @Test
    public void keepLinkedinUrlAfterConversion(){
        Candidate candidate = new Candidate();
        String linkedinUrl = "linkedinUrl";
        candidate.setLinkedinUrl(linkedinUrl);
        CandidateDTO candidateDTO = this.candidateToCandidateDTO.convert(candidate);


        assertEquals(candidateDTO.getLinkedinUrl(),linkedinUrl);
    }

    @Test
    public void keepSourceAfterConversion(){
        Candidate candidate = new Candidate();
        String source = "source";
        candidate.setSource(source);
        CandidateDTO candidateDTO = this.candidateToCandidateDTO.convert(candidate);


        assertEquals(candidateDTO.getSource(),source);
    }

    @Test
    public void keepLocationAfterConversion(){
        Candidate candidate = new Candidate();
        String location = "location";
        candidate.setLocation(location);
        CandidateDTO candidateDTO = this.candidateToCandidateDTO.convert(candidate);


        assertEquals(candidateDTO.getLocation(),location);
    }

    @Test
    public void keepDiverseAfterConversion(){
        Candidate candidate = new Candidate();
        String diverese = "diverese";
        candidate.setDiverse(diverese);
        CandidateDTO candidateDTO = this.candidateToCandidateDTO.convert(candidate);


        assertEquals(candidateDTO.getDiverse(),diverese);
    }

    @Test
    public void keepCompanyAfterConversion(){
        Candidate candidate = new Candidate();
        String company = "company";
        candidate.setCompany(company);
        CandidateDTO candidateDTO = this.candidateToCandidateDTO.convert(candidate);


        assertEquals(candidateDTO.getCompany(),company);
    }

    @Test
    public void keepRoleAfterConversion(){
        Candidate candidate = new Candidate();
        String role = "role";
        candidate.setRole(role);
        CandidateDTO candidateDTO = this.candidateToCandidateDTO.convert(candidate);


        assertEquals(candidateDTO.getRole(),role);
    }

    @Test
    public void keepCommentAfterConversion(){
        Candidate candidate = new Candidate();
        String comment = "comment";
        candidate.setComment(comment);
        CandidateDTO candidateDTO = this.candidateToCandidateDTO.convert(candidate);


        assertEquals(candidateDTO.getComment(),comment);
    }

    @Test
    public void keepIdAfterConversion(){
        Candidate candidate = new Candidate();
        Long id = 1L;
        candidate.setId(id);
        CandidateDTO candidateDTO = this.candidateToCandidateDTO.convert(candidate);

        assertEquals(candidateDTO.getId(),id);
    }

    @Test
    public void keepConsultantAfterConversion(){
        Candidate candidate = new Candidate();
        String consultant = "consultant";
        candidate.setConsultant(consultant);
        CandidateDTO candidateDTO = this.candidateToCandidateDTO.convert(candidate);


        assertEquals(candidateDTO.getConsultant(),consultant);
    }

}