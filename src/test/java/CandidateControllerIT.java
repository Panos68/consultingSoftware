import com.consultant.model.dto.CandidateDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CandidateControllerIT
        extends AbstractControllerIT {

    @Test
    public void testCandidateRetrieving() throws Exception {
        List<CandidateDTO> candidates = getCandidatesDTOList();

        assertThat(candidates.stream()
                .map(CandidateDTO::getLinkedinUrl)
                .anyMatch(u -> u.equals("MainLinkedIn")))
                .isTrue();
    }

    @Test
    public void testCandidateCreation() throws Exception {
        CandidateDTO candidateDTO = new CandidateDTO();
        candidateDTO.setLinkedinUrl("CreatedLinkedIn");

        setupAuth(NON_ADMIN_EMAIL);

        mockMvc.perform(post("/candidates")
                .content(objectMapper.writeValueAsString(candidateDTO))
                .contentType("application/json")
                .session(session))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<CandidateDTO> candidates = getCandidatesDTOList();

        assertThat(candidates.stream()
                .map(CandidateDTO::getLinkedinUrl)
                .anyMatch(u -> u.equals("CreatedLinkedIn")))
                .isTrue();
    }

    @Test
    public void testCandidateDeletion() throws Exception {
        setupAuth(NON_ADMIN_EMAIL);

        mockMvc.perform(delete("/candidates/3")
                .session(session))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<CandidateDTO> candidates = getCandidatesDTOList();

        assertThat(candidates.stream()
                .map(CandidateDTO::getLinkedinUrl)
                .anyMatch(u -> u.equals("DeleteLinkedIn")))
                .isFalse();
    }

    @Test
    public void testCandidateEditing() throws Exception {
        setupAuth(NON_ADMIN_EMAIL);

        CandidateDTO editedCandidate = new CandidateDTO();
        editedCandidate.setId(2L);
        editedCandidate.setLinkedinUrl("EditedLinkedIn");

        mockMvc.perform(put("/candidates")
                .content(objectMapper.writeValueAsString(editedCandidate))
                .contentType("application/json")
                .session(session))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<CandidateDTO> candidates = getCandidatesDTOList();

        assertThat(candidates.stream()
                .map(CandidateDTO::getLinkedinUrl)
                .anyMatch(u -> u.equals("EditedLinkedIn")))
                .isTrue();
    }

    private List<CandidateDTO> getCandidatesDTOList() throws Exception {
        setupAuth(NON_ADMIN_EMAIL);

        String responseBodyAsString = mockMvc.perform(get("/candidates")
                .session(session))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        CandidateDTO[] candidateDTOS = objectMapper.readValue(responseBodyAsString, CandidateDTO[].class);
        return List.of(candidateDTOS);
    }
}