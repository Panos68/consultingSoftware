import com.consultant.model.dto.ClientTeamDTO;
import com.consultant.model.dto.ConsultantDTO;
import com.consultant.model.entities.Consultant;
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

public class ConsultantControllerIT extends AbstractControllerIT {

    Gson gson = new Gson();

    @Test
    public void testConsultantsRetrieving() throws Exception {
        addAuthorizationRequestToHeader();

        HttpEntity<ConsultantDTO> getConsultantsEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/consultants"), HttpMethod.GET, getConsultantsEntity, String.class);
        Type type = new TypeToken<List<ConsultantDTO>>() {
        }.getType();

        List<ConsultantDTO> consultantDTOS = gson.fromJson(response.getBody(), type);

        assertTrue(Objects.requireNonNull(consultantDTOS).stream().map(ConsultantDTO::getId).anyMatch(u -> u.equals(1L)));
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }


    @Test
    public void testConsultantCreation() throws Exception {
        ConsultantDTO consultantDTO = new ConsultantDTO();
        consultantDTO.setFirstName("Created");
        consultantDTO.setTeamId(1L);

        addAuthorizationRequestToHeader();

        HttpEntity<ConsultantDTO> entity = new HttpEntity<>(consultantDTO, headers);
        ResponseEntity<String> createConsultantResponse = restTemplate.exchange(
                createURLWithPort("/consultants"), HttpMethod.POST, entity, String.class);

        HttpEntity<ConsultantDTO> getConsultantsEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/consultants"), HttpMethod.GET, getConsultantsEntity, String.class);
        Type type = new TypeToken<List<ConsultantDTO>>() {
        }.getType();

        List<ConsultantDTO> consultantDTOS = gson.fromJson(response.getBody(), type);

        HttpEntity<ClientTeamDTO> getClientTeamsEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> teamsResponse = restTemplate.exchange(
                createURLWithPort("/teams"), HttpMethod.GET, getClientTeamsEntity, String.class);
        Type teamsType = new TypeToken<List<ClientTeamDTO>>() {}.getType();
        List<ClientTeamDTO> clientTeamDTOS = gson.fromJson(teamsResponse.getBody(), teamsType);

        boolean consultantExistsForTeam = clientTeamDTOS.stream()
                .filter(clientTeamDTO -> clientTeamDTO.getId() == 1).map(ClientTeamDTO::getConsultants)
                .flatMap(Collection::stream)
                .anyMatch(clientTeam -> clientTeam.getFirstName().equals("Created"));

        assertTrue(consultantExistsForTeam);
        assertTrue(Objects.requireNonNull(consultantDTOS).stream().map(ConsultantDTO::getFirstName).anyMatch(u -> u.equals("Created")));
        assertEquals(createConsultantResponse.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testConsultantDeletion() throws Exception {
        addAuthorizationRequestToHeader();

        HttpEntity<ConsultantDTO> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> deleteConsultantResponse = restTemplate.exchange(
                createURLWithPort("/consultants/3"), HttpMethod.DELETE, entity, String.class);

        HttpEntity<ConsultantDTO> getConsultantsEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/consultants"), HttpMethod.GET, getConsultantsEntity, String.class);
        Type type = new TypeToken<List<ConsultantDTO>>() {
        }.getType();

        List<ConsultantDTO> consultantDTOS = gson.fromJson(response.getBody(), type);

        assertFalse(Objects.requireNonNull(consultantDTOS).stream().map(ConsultantDTO::getFirstName).anyMatch(u -> u.equals("DeleteConsultant")));
        assertEquals(deleteConsultantResponse.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testConsultantEditing() throws Exception {
        addAuthorizationRequestToHeader();

        ConsultantDTO consultantDTO = new ConsultantDTO();
        consultantDTO.setId(2L);
        consultantDTO.setFirstName("EditedConsultant");

        HttpEntity<ConsultantDTO> entity = new HttpEntity<>(consultantDTO, headers);
        ResponseEntity<String> editConsultantResponse = restTemplate.exchange(
                createURLWithPort("/consultants"), HttpMethod.PUT, entity, String.class);

        HttpEntity<ConsultantDTO> getConsultantsEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/consultants"), HttpMethod.GET, getConsultantsEntity, String.class);
        Type type = new TypeToken<List<ConsultantDTO>>() {
        }.getType();

        List<ConsultantDTO> consultantDTOS = gson.fromJson(response.getBody(), type);

        assertTrue(Objects.requireNonNull(consultantDTOS).stream().map(ConsultantDTO::getFirstName).anyMatch(u -> u.equals("EditedConsultant")));
        assertEquals(editConsultantResponse.getStatusCode(), HttpStatus.OK);
    }
}