import com.consultant.model.dto.ClientDTO;
import com.consultant.model.dto.ClientTeamDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ClientControllerIT extends AbstractControllerIT {

    @Test
    public void testClientsRetrieving() throws Exception {
        setupAuth(NON_ADMIN_EMAIL);

        List<ClientDTO> clientDTOS = getClientDTOS();

        assertThat(clientDTOS.stream()
                .map(ClientDTO::getId)
                .anyMatch(u -> u.equals(1L)))
                .isTrue();
    }

    @Test
    public void testClientCreation() throws Exception {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setName("Created");

        setupAuth(NON_ADMIN_EMAIL);

        mockMvc.perform(post("/clients")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(clientDTO))
                .session(session))
                .andExpect(status().isOk());

        List<ClientDTO> clientDTOS = getClientDTOS();

        assertThat(clientDTOS.stream()
                .map(ClientDTO::getName)
                .anyMatch(u -> u.equals("Created")))
                .isTrue();
    }

    @Test
    public void testClientDeletion() throws Exception {
        setupAuth(NON_ADMIN_EMAIL);

        mockMvc.perform(delete("/clients/3")
                .session(session))
                .andExpect(status().isOk());

        List<ClientDTO> clientDTOS = getClientDTOS();

        String teamDTOSAsString = mockMvc.perform(get("/teams")
                .session(session))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ClientTeamDTO[] teamDTOS = objectMapper.readValue(teamDTOSAsString, ClientTeamDTO[].class);

        assertThat(List.of(teamDTOS)
                .stream()
                .anyMatch(clientTeamDTO -> clientTeamDTO.getName().equals("DeleteTeam2")))
                .isTrue();
        assertThat(clientDTOS.stream().anyMatch(ClientDTO::getDeleted)).isTrue();
    }

    @Test
    public void testClientEditing() throws Exception {
        setupAuth(NON_ADMIN_EMAIL);

        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(2L);
        clientDTO.setName("EditedClient");

        mockMvc.perform(put("/clients")
                .content(objectMapper.writeValueAsString(clientDTO))
                .contentType("application/json")
                .session(session))
                .andExpect(status().isOk());

        List<ClientDTO> clientDTOS = getClientDTOS();

        assertThat(clientDTOS.stream()
                .map(ClientDTO::getName)
                .anyMatch(u -> u.equals("EditedClient")))
                .isTrue();
    }

    private List<ClientDTO> getClientDTOS() throws Exception {
        setupAuth("nonAdminUser@mirado.com");

        String clientDTOSAsString = mockMvc.perform(get("/clients")
                .session(session))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ClientDTO[] clientDTOS = objectMapper.readValue(clientDTOSAsString, ClientDTO[].class);

        return List.of(clientDTOS);
    }
}