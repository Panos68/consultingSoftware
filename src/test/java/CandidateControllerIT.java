import com.consultant.model.dto.UserDTO;
import com.consultant.model.dto.CandidateDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class CandidateControllerIT
//        extends AbstractControllerIT
{

//    @Test
//    public void testCandidateRetrieving() throws Exception {
//        addAuthorizationRequestToHeader();
//
//        HttpEntity<CandidateDTO> getCandidatesEntity = new HttpEntity<>(null, headers);
//        ResponseEntity<String> response = restTemplate.exchange(
//                createURLWithPort("/candidates"), HttpMethod.GET, getCandidatesEntity, String.class);
//        Type type = new TypeToken<List<CandidateDTO>>() {}.getType();
//
//        List<CandidateDTO> candidatesList = gson.fromJson(response.getBody(), type);
//
//        assertTrue(Objects.requireNonNull(candidatesList).stream().map(CandidateDTO::getLinkedinUrl).anyMatch(u -> u.equals("MainLinkedIn")));
//        assertEquals(response.getStatusCode(), HttpStatus.OK);
//    }
//
//    @Test
//    public void testCandidateCreation() throws Exception {
//        CandidateDTO candidateDTO = new CandidateDTO();
//        candidateDTO.setLinkedinUrl("CreatedLinkedIn");
//
//        addAuthorizationRequestToHeader();
//
//        HttpEntity<CandidateDTO> entity = new HttpEntity<>(candidateDTO, headers);
//        ResponseEntity<String> createCandidateResponse = restTemplate.exchange(
//                createURLWithPort("/candidates"), HttpMethod.POST, entity, String.class);
//
//        List<CandidateDTO> candidatesList = getCandidatesDTOList();
//
//        assertTrue(Objects.requireNonNull(candidatesList).stream().map(CandidateDTO::getLinkedinUrl).anyMatch(u -> u.equals("CreatedLinkedIn")));
//        assertEquals(createCandidateResponse.getStatusCode(), HttpStatus.OK);
//    }
//
//    @Test
//    public void testCandidateDeletion() throws Exception {
//        addAuthorizationRequestToHeader();
//
//        HttpEntity<CandidateDTO> entity = new HttpEntity<>(null, headers);
//        ResponseEntity<String> deleteCandidateResponse = restTemplate.exchange(
//                createURLWithPort("/candidates/3"), HttpMethod.DELETE, entity, String.class);
//
//        List<CandidateDTO> candidatesList = getCandidatesDTOList();
//
//        assertFalse(Objects.requireNonNull(candidatesList).stream().map(CandidateDTO::getLinkedinUrl).anyMatch(u -> u.equals("DeleteLinkedIn")));
//        assertEquals(deleteCandidateResponse.getStatusCode(), HttpStatus.OK);
//    }
//
//    @Test
//    public void testCandidateEditing() throws Exception {
//        addAuthorizationRequestToHeader();
//
//        CandidateDTO editedCandidate = new CandidateDTO();
//        editedCandidate.setId(2L);
//        editedCandidate.setLinkedinUrl("EditedLinkedIn");
//
//        HttpEntity<CandidateDTO> entity = new HttpEntity<>(editedCandidate, headers);
//        ResponseEntity<String> editCandidateResponse = restTemplate.exchange(
//                createURLWithPort("/candidates"), HttpMethod.PUT, entity, String.class);
//
//        List<CandidateDTO> candidatesList = getCandidatesDTOList();
//
//        assertTrue(Objects.requireNonNull(candidatesList).stream().map(CandidateDTO::getLinkedinUrl).anyMatch(u -> u.equals("EditedLinkedIn")));
//        assertEquals(editCandidateResponse.getStatusCode(), HttpStatus.OK);
//    }
//
//    private List<CandidateDTO> getCandidatesDTOList() {
//        HttpEntity<CandidateDTO> getCandidatesEntity = new HttpEntity<>(null, headers);
//        ResponseEntity<String> response = restTemplate.exchange(
//                createURLWithPort("/candidates"), HttpMethod.GET, getCandidatesEntity, String.class);
//
//        Type type = new TypeToken<List<CandidateDTO>>() {}.getType();
//        return gson.fromJson(response.getBody(), type);
//    }
}