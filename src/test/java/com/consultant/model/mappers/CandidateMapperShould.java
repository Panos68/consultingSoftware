package com.consultant.model.mappers;

import com.consultant.model.dto.CandidateDTO;
import com.consultant.model.entities.Candidate;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CandidateMapperShould {

    @Test
    public void keepLinkedinUrlAfterConvertingToDTO(){
        Candidate candidate = new Candidate();
        String linkedinUrl = "linkedinUrl";
        candidate.setLinkedinUrl(linkedinUrl);
        CandidateDTO candidateDTO = CandidateMapper.INSTANCE.candidateToCandidateDTO( candidate );


        assertEquals(candidateDTO.getLinkedinUrl(),linkedinUrl);
    }

    @Test
    public void keepSourceAfterConvertingToDTO(){
        Candidate candidate = new Candidate();
        String source = "source";
        candidate.setSource(source);
        CandidateDTO candidateDTO = CandidateMapper.INSTANCE.candidateToCandidateDTO( candidate );


        assertEquals(candidateDTO.getSource(),source);
    }

    @Test
    public void keepLocationAfterConvertingToDTO(){
        Candidate candidate = new Candidate();
        String location = "location";
        candidate.setLocation(location);
        CandidateDTO candidateDTO = CandidateMapper.INSTANCE.candidateToCandidateDTO( candidate );


        assertEquals(candidateDTO.getLocation(),location);
    }

    @Test
    public void keepDiverseAfterConvertingToDTO(){
        Candidate candidate = new Candidate();
        String diverese = "diverese";
        candidate.setDiverse(diverese);
        CandidateDTO candidateDTO = CandidateMapper.INSTANCE.candidateToCandidateDTO( candidate );


        assertEquals(candidateDTO.getDiverse(),diverese);
    }

    @Test
    public void keepCompanyAfterConvertingToDTO(){
        Candidate candidate = new Candidate();
        String company = "company";
        candidate.setCompany(company);
        CandidateDTO candidateDTO = CandidateMapper.INSTANCE.candidateToCandidateDTO( candidate );


        assertEquals(candidateDTO.getCompany(),company);
    }

    @Test
    public void keepRoleAfterConvertingToDTO(){
        Candidate candidate = new Candidate();
        String role = "role";
        candidate.setRole(role);
        CandidateDTO candidateDTO = CandidateMapper.INSTANCE.candidateToCandidateDTO( candidate );


        assertEquals(candidateDTO.getRole(),role);
    }

    @Test
    public void keepCommentAfterConvertingToDTO(){
        Candidate candidate = new Candidate();
        String comment = "comment";
        candidate.setComment(comment);
        CandidateDTO candidateDTO = CandidateMapper.INSTANCE.candidateToCandidateDTO( candidate );


        assertEquals(candidateDTO.getComment(),comment);
    }

    @Test
    public void keepIdAfterConvertingToDTO(){
        Candidate candidate = new Candidate();
        Long id = 1L;
        candidate.setId(id);
        CandidateDTO candidateDTO = CandidateMapper.INSTANCE.candidateToCandidateDTO( candidate );

        assertEquals(candidateDTO.getId(),id);
    }

    @Test
    public void keepConsultantAfterConvertingToDTO(){
        Candidate candidate = new Candidate();
        String consultant = "consultant";
        candidate.setConsultant(consultant);
        CandidateDTO candidateDTO = CandidateMapper.INSTANCE.candidateToCandidateDTO( candidate );


        assertEquals(candidateDTO.getConsultant(),consultant);
    }

    @Test
    public void keepLinkedinUrlAfterConvertingDTO(){
        CandidateDTO candidateDTO = new CandidateDTO();
        String linkedinUrl = "linkedinUrl";
        candidateDTO.setLinkedinUrl(linkedinUrl);
        Candidate candidate = CandidateMapper.INSTANCE.candidateDTOToCandidate( candidateDTO );

        assertEquals(candidate.getLinkedinUrl(),linkedinUrl);
    }

    @Test
    public void keepSourceAfterConvertingDTO(){
        CandidateDTO candidateDTO = new CandidateDTO();
        String source = "source";
        candidateDTO.setSource(source);
        Candidate candidate = CandidateMapper.INSTANCE.candidateDTOToCandidate( candidateDTO );

        assertEquals(candidate.getSource(),source);
    }

    @Test
    public void keepLocationAfterConvertingDTO(){
        CandidateDTO candidateDTO = new CandidateDTO();
        String location = "location";
        candidateDTO.setLocation(location);
        Candidate candidate = CandidateMapper.INSTANCE.candidateDTOToCandidate( candidateDTO );

        assertEquals(candidate.getLocation(),location);
    }

    @Test
    public void keepDiverseAfterConvertingDTO(){
        CandidateDTO candidateDTO = new CandidateDTO();
        String diverese = "diverese";
        candidateDTO.setDiverse(diverese);
        Candidate candidate = CandidateMapper.INSTANCE.candidateDTOToCandidate( candidateDTO );

        assertEquals(candidate.getDiverse(),diverese);
    }

    @Test
    public void keepCompanyAfterConvertingDTO(){
        CandidateDTO candidateDTO = new CandidateDTO();
        String company = "company";
        candidateDTO.setCompany(company);
        Candidate candidate = CandidateMapper.INSTANCE.candidateDTOToCandidate( candidateDTO );

        assertEquals(candidate.getCompany(),company);
    }

    @Test
    public void keepRoleAfterConvertingDTO(){
        CandidateDTO candidateDTO = new CandidateDTO();
        String role = "role";
        candidateDTO.setRole(role);
        Candidate candidate = CandidateMapper.INSTANCE.candidateDTOToCandidate( candidateDTO );

        assertEquals(candidate.getRole(),role);
    }

    @Test
    public void keepCommentAfterConvertingDTO(){
        CandidateDTO candidateDTO = new CandidateDTO();
        String comment = "comment";
        candidateDTO.setComment(comment);
        Candidate candidate = CandidateMapper.INSTANCE.candidateDTOToCandidate( candidateDTO );

        assertEquals(candidate.getComment(),comment);
    }

    @Test
    public void keepIdAfterConvertingDTO(){
        CandidateDTO candidateDTO = new CandidateDTO();
        Long id = 1L;
        candidateDTO.setId(id);
        Candidate candidate = CandidateMapper.INSTANCE.candidateDTOToCandidate( candidateDTO );

        assertEquals(candidate.getId(),id);
    }

    @Test
    public void keepConsultantAfterConvertingDTO(){
        CandidateDTO candidateDTO = new CandidateDTO();
        String consultant = "consultant";
        candidateDTO.setConsultant(consultant);
        Candidate candidate = CandidateMapper.INSTANCE.candidateDTOToCandidate( candidateDTO );

        assertEquals(candidate.getConsultant(),consultant);
    }

}