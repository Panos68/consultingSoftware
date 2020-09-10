import com.consultant.model.dto.ClientTeamDTO;
import com.consultant.model.dto.ConsultantDTO;
import com.consultant.model.dto.ContractDTO;
import com.consultant.model.entities.Contract;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConsultantControllerIT
//        extends AbstractControllerIT
{

//    @Test
//    public void testConsultantsRetrieving() throws Exception {
//        addAuthorizationRequestToHeader();
//
//        HttpEntity<ConsultantDTO> getConsultantsEntity = new HttpEntity<>(null, headers);
//        ResponseEntity<String> response = restTemplate.exchange(
//                createURLWithPort("/consultants"), HttpMethod.GET, getConsultantsEntity, String.class);
//        Type type = new TypeToken<List<ConsultantDTO>>() {
//        }.getType();
//
//        List<ConsultantDTO> consultantDTOS = gson.fromJson(response.getBody(), type);
//
//        assertTrue(Objects.requireNonNull(consultantDTOS).stream().map(ConsultantDTO::getId).anyMatch(u -> u.equals(1L)));
//        assertEquals(HttpStatus.OK,response.getStatusCode());
//    }
//
//
//    @Test
//    public void testConsultantCreation() throws Exception {
//        ConsultantDTO consultantDTO = new ConsultantDTO();
//        consultantDTO.setFirstName("Created");
//        consultantDTO.setTeamId(1L);
//        consultantDTO.setActiveContract(new ContractDTO());
//
//        addAuthorizationRequestToHeader();
//
//        HttpEntity<ConsultantDTO> entity = new HttpEntity<>(consultantDTO, headers);
//        ResponseEntity<String> createConsultantResponse = restTemplate.exchange(
//                createURLWithPort("/consultants"), HttpMethod.POST, entity, String.class);
//
//        HttpEntity<ConsultantDTO> getConsultantsEntity = new HttpEntity<>(null, headers);
//        ResponseEntity<String> response = restTemplate.exchange(
//                createURLWithPort("/consultants"), HttpMethod.GET, getConsultantsEntity, String.class);
//        Type type = new TypeToken<List<ConsultantDTO>>() {
//        }.getType();
//
//        List<ConsultantDTO> consultantDTOS = gson.fromJson(response.getBody(), type);
//
//        HttpEntity<ClientTeamDTO> getClientTeamsEntity = new HttpEntity<>(null, headers);
//        ResponseEntity<String> teamsResponse = restTemplate.exchange(
//                createURLWithPort("/teams"), HttpMethod.GET, getClientTeamsEntity, String.class);
//        Type teamsType = new TypeToken<List<ClientTeamDTO>>() {}.getType();
//        List<ClientTeamDTO> clientTeamDTOS = gson.fromJson(teamsResponse.getBody(), teamsType);
//
//        boolean consultantExistsForTeam = clientTeamDTOS.stream()
//                .filter(clientTeamDTO -> clientTeamDTO.getId() == 1).map(ClientTeamDTO::getConsultants)
//                .flatMap(Collection::stream)
//                .anyMatch(clientTeam -> clientTeam.getFirstName().equals("Created"));
//
//        assertTrue(consultantExistsForTeam);
//        assertTrue(Objects.requireNonNull(consultantDTOS).stream().map(ConsultantDTO::getFirstName).anyMatch(u -> u.equals("Created")));
//        assertEquals(HttpStatus.OK,createConsultantResponse.getStatusCode());
//    }
//
//    @Test
//    public void testConsultantDeletion() throws Exception {
//        addAuthorizationRequestToHeader();
//
//        HttpEntity<ConsultantDTO> entity = new HttpEntity<>(null, headers);
//        ResponseEntity<String> deleteConsultantResponse = restTemplate.exchange(
//                createURLWithPort("/consultants/3/?terminatedDate=2020-01-01"), HttpMethod.DELETE, entity, String.class);
//
//        HttpEntity<ConsultantDTO> getConsultantsEntity = new HttpEntity<>(null, headers);
//        ResponseEntity<String> response = restTemplate.exchange(
//                createURLWithPort("/consultants"), HttpMethod.GET, getConsultantsEntity, String.class);
//        Type type = new TypeToken<List<ConsultantDTO>>() {
//        }.getType();
//
//        List<ConsultantDTO> consultantDTOS = gson.fromJson(response.getBody(), type);
//
//        Optional<ConsultantDTO> deletedConsultant = Objects.requireNonNull(consultantDTOS).stream()
//                .filter(ConsultantDTO::getDeleted)
//                .findFirst();
//
//        assertTrue(deletedConsultant.isPresent());
//        assertFalse(deletedConsultant.get()
//                .getContracts()
//                .stream()
//                .anyMatch(ContractDTO::getActive));
//        assertEquals(HttpStatus.OK,deleteConsultantResponse.getStatusCode());
//    }
//
//    @Test
//    public void testConsultantEditing() throws Exception {
//        addAuthorizationRequestToHeader();
//
//        ConsultantDTO consultantDTO = new ConsultantDTO();
//        consultantDTO.setId(2L);
//        consultantDTO.setFirstName("EditedConsultant");
//        consultantDTO.setActiveContract(new ContractDTO());
//
//        HttpEntity<ConsultantDTO> entity = new HttpEntity<>(consultantDTO, headers);
//        ResponseEntity<String> editConsultantResponse = restTemplate.exchange(
//                createURLWithPort("/consultants"), HttpMethod.PUT, entity, String.class);
//
//        HttpEntity<ConsultantDTO> getConsultantsEntity = new HttpEntity<>(null, headers);
//        ResponseEntity<String> response = restTemplate.exchange(
//                createURLWithPort("/consultants"), HttpMethod.GET, getConsultantsEntity, String.class);
//        Type type = new TypeToken<List<ConsultantDTO>>() {
//        }.getType();
//
//        List<ConsultantDTO> consultantDTOS = gson.fromJson(response.getBody(), type);
//
//        assertTrue(Objects.requireNonNull(consultantDTOS).stream().map(ConsultantDTO::getFirstName).anyMatch(u -> u.equals("EditedConsultant")));
//        assertEquals(HttpStatus.OK,editConsultantResponse.getStatusCode());
//    }
}