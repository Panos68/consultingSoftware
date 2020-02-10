import com.consultant.model.dto.ClientDTO;
import com.consultant.model.dto.ClientTeamDTO;
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

public class ClientTeamControllerIT extends AbstractControllerIT {

    Gson gson = new Gson();

    @Test
    public void testClientTeamsRetrieving() throws Exception {
        addAuthorizationRequestToHeader();

        HttpEntity<ClientTeamDTO> getClientTeamsEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/teams"), HttpMethod.GET, getClientTeamsEntity, String.class);
        Type type = new TypeToken<List<ClientTeamDTO>>() {}.getType();

        List<ClientTeamDTO> clientTeamDTOS = gson.fromJson(response.getBody(), type);

        assertTrue(Objects.requireNonNull(clientTeamDTOS).stream().map(ClientTeamDTO::getId).anyMatch(u -> u.equals(1L)));
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }


    @Test
    public void testClientTeamCreation() throws Exception {
        ClientTeamDTO clientTeamDTO = new ClientTeamDTO();
        clientTeamDTO.setName("Created");
        clientTeamDTO.setClientId(1L);

        addAuthorizationRequestToHeader();

        HttpEntity<ClientTeamDTO> entity = new HttpEntity<>(clientTeamDTO, headers);
        ResponseEntity<String> createClientTeamResponse = restTemplate.exchange(
                createURLWithPort("/teams"), HttpMethod.POST, entity, String.class);

        HttpEntity<ClientTeamDTO> getClientTeamsEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/teams"), HttpMethod.GET, getClientTeamsEntity, String.class);
        Type type = new TypeToken<List<ClientTeamDTO>>() {}.getType();

        List<ClientTeamDTO> clientTeamDTOS = gson.fromJson(response.getBody(), type);

        HttpEntity<ClientDTO> getClientsEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> clientResponse = restTemplate.exchange(
                createURLWithPort("/clients"), HttpMethod.GET, getClientsEntity, String.class);
        Type clientType = new TypeToken<List<ClientDTO>>() {}.getType();

        List<ClientDTO> clientDTOS = gson.fromJson(clientResponse.getBody(), clientType);

        boolean clientTeamExistsForClient = clientDTOS.stream()
                .filter(clientDTO -> clientDTO.getId() == 1).map(ClientDTO::getClientTeams)
                .flatMap(Collection::stream)
                .anyMatch(clientTeam -> clientTeam.getName().equals("Created"));

        assertTrue(clientTeamExistsForClient);
        assertTrue(Objects.requireNonNull(clientTeamDTOS).stream().map(ClientTeamDTO::getName).anyMatch(u -> u.equals("Created")));
        assertEquals(createClientTeamResponse.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testClientTeamDeletion() throws Exception {
        addAuthorizationRequestToHeader();

        HttpEntity<ClientTeamDTO> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> deleteClientTeamResponse = restTemplate.exchange(
                createURLWithPort("/teams/3"), HttpMethod.DELETE, entity, String.class);

        HttpEntity<ClientTeamDTO> getClientTeamsEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/teams"), HttpMethod.GET, getClientTeamsEntity, String.class);
        Type type = new TypeToken<List<ClientTeamDTO>>() {}.getType();

        List<ClientTeamDTO> clientTeamDTOS = gson.fromJson(response.getBody(), type);

        assertFalse(Objects.requireNonNull(clientTeamDTOS).stream().map(ClientTeamDTO::getName).anyMatch(u -> u.equals("DeleteClientTeam")));
        assertEquals(deleteClientTeamResponse.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testClientTeamEditing() throws Exception {
        addAuthorizationRequestToHeader();

        ClientTeamDTO clientTeamDTO = new ClientTeamDTO();
        clientTeamDTO.setId(2L);
        clientTeamDTO.setName("EditedClientTeam");

        HttpEntity<ClientTeamDTO> entity = new HttpEntity<>(clientTeamDTO, headers);
        ResponseEntity<String> editClientTeamResponse = restTemplate.exchange(
                createURLWithPort("/teams"), HttpMethod.PUT, entity, String.class);

        HttpEntity<ClientTeamDTO> getClientTeamsEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/teams"), HttpMethod.GET, getClientTeamsEntity, String.class);
        Type type = new TypeToken<List<ClientTeamDTO>>() {}.getType();

        List<ClientTeamDTO> clientTeamDTOS = gson.fromJson(response.getBody(), type);

        assertTrue(Objects.requireNonNull(clientTeamDTOS).stream().map(ClientTeamDTO::getName).anyMatch(u -> u.equals("EditedClientTeam")));
        assertEquals(editClientTeamResponse.getStatusCode(), HttpStatus.OK);
    }
}