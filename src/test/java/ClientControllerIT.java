import com.consultant.model.dto.ClientDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class ClientControllerIT extends AbstractControllerIT {

    Gson gson = new Gson();

    @Test
    public void testClientsRetrieving() throws Exception {
        addAuthorizationRequestToHeader();

        HttpEntity<ClientDTO> getClientsEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/clients"), HttpMethod.GET, getClientsEntity, String.class);
        Type type = new TypeToken<List<ClientDTO>>() {}.getType();

        List<ClientDTO> clientDTOS = gson.fromJson(response.getBody(), type);

        assertTrue(Objects.requireNonNull(clientDTOS).stream().map(ClientDTO::getId).anyMatch(u -> u.equals(1L)));
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }


    @Test
    public void testClientCreation() throws Exception {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setName("Created");

        addAuthorizationRequestToHeader();

        HttpEntity<ClientDTO> entity = new HttpEntity<>(clientDTO, headers);
        ResponseEntity<String> createClientResponse = restTemplate.exchange(
                createURLWithPort("/clients"), HttpMethod.POST, entity, String.class);

        HttpEntity<ClientDTO> getClientsEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/clients"), HttpMethod.GET, getClientsEntity, String.class);
        Type type = new TypeToken<List<ClientDTO>>() {}.getType();

        List<ClientDTO> clientDTOS = gson.fromJson(response.getBody(), type);

        assertTrue(Objects.requireNonNull(clientDTOS).stream().map(ClientDTO::getName).anyMatch(u -> u.equals("Created")));
        assertEquals(createClientResponse.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testClientDeletion() throws Exception {
        addAuthorizationRequestToHeader();

        HttpEntity<ClientDTO> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> deleteClientResponse = restTemplate.exchange(
                createURLWithPort("/clients/3"), HttpMethod.DELETE, entity, String.class);

        HttpEntity<ClientDTO> getClientsEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/clients"), HttpMethod.GET, getClientsEntity, String.class);
        Type type = new TypeToken<List<ClientDTO>>() {}.getType();

        List<ClientDTO> clientDTOS = gson.fromJson(response.getBody(), type);

        assertFalse(Objects.requireNonNull(clientDTOS).stream().map(ClientDTO::getName).anyMatch(u -> u.equals("Delete")));
        assertEquals(deleteClientResponse.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testClientEditing() throws Exception {
        addAuthorizationRequestToHeader();

        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(2L);
        clientDTO.setName("EditedClient");

        HttpEntity<ClientDTO> entity = new HttpEntity<>(clientDTO, headers);
        ResponseEntity<String> editClientResponse = restTemplate.exchange(
                createURLWithPort("/clients"), HttpMethod.PUT, entity, String.class);

        HttpEntity<ClientDTO> getConsultantsEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/clients"), HttpMethod.GET, getConsultantsEntity, String.class);
        Type type = new TypeToken<List<ClientDTO>>() {}.getType();

        List<ClientDTO> clientDTOS = gson.fromJson(response.getBody(), type);

        assertTrue(Objects.requireNonNull(clientDTOS).stream().map(ClientDTO::getName).anyMatch(u -> u.equals("EditedClient")));
        assertEquals(editClientResponse.getStatusCode(), HttpStatus.OK);
    }
}