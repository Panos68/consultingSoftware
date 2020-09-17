import com.consultant.model.dto.ClientDTO;
import com.consultant.model.dto.ClientTeamDTO;
import com.consultant.model.dto.ConsultantDTO;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ClientTeamControllerIT extends AbstractControllerIT {

    @Test
    public void testClientTeamsRetrieving() throws Exception {
        setupAuth(NON_ADMIN_EMAIL);

        List<ClientTeamDTO> clientTeamDTOS = getClientTeamDTOS();

        assertThat(clientTeamDTOS.stream()
                .map(ClientTeamDTO::getId)
                .anyMatch(u -> u.equals(1L)))
                .isTrue();
    }

    @Test
    public void testClientTeamCreation() throws Exception {
        ClientTeamDTO clientTeamDTO = new ClientTeamDTO();
        clientTeamDTO.setName("Created");
        clientTeamDTO.setClientId(1L);

        setupAuth(NON_ADMIN_EMAIL);

        mockMvc.perform(post("/teams")
                .content(objectMapper.writeValueAsString(clientTeamDTO))
                .contentType("application/json")
                .session(session))
                .andExpect(status().isOk());

        List<ClientTeamDTO> clientTeamDTOS = getClientTeamDTOS();

        String clientDTOSAsString = mockMvc.perform(get("/clients")
                .session(session))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ClientDTO[] clientDTOS = objectMapper.readValue(clientDTOSAsString, ClientDTO[].class);

        boolean clientTeamExistsForClient = List.of(clientDTOS).stream()
                .filter(clientDTO -> clientDTO.getId() == 1).map(ClientDTO::getClientTeams)
                .flatMap(Collection::stream)
                .anyMatch(clientTeam -> clientTeam.getName().equals("Created"));

        assertThat(clientTeamExistsForClient).isTrue();
        assertThat(clientTeamDTOS.stream()
                .map(ClientTeamDTO::getName)
                .anyMatch(u -> u.equals("Created")))
                .isTrue();
    }

    @Test
    public void testClientTeamDeletion() throws Exception {
        setupAuth(NON_ADMIN_EMAIL);

        mockMvc.perform(delete("/teams/3")
                .session(session))
                .andExpect(status().isOk());

        List<ClientTeamDTO> clientTeamDTOS = getClientTeamDTOS();

        String consultantDTOSAsString = mockMvc.perform(get("/consultants")
                .session(session))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ConsultantDTO[] consultantDTOS = objectMapper.readValue(consultantDTOSAsString, ConsultantDTO[].class);

        assertThat(List.of(consultantDTOS)
                .stream()
                .anyMatch(consultantDTO -> consultantDTO.getFirstName().equals("MainConsultant")))
                .isTrue();
        assertThat(clientTeamDTOS
                .stream()
                .anyMatch(ClientTeamDTO::getDeleted))
                .isTrue();
    }

    @Test
    public void testClientTeamEditing() throws Exception {
        setupAuth(NON_ADMIN_EMAIL);

        ClientTeamDTO clientTeamDTO = new ClientTeamDTO();
        clientTeamDTO.setId(2L);
        clientTeamDTO.setName("EditedClientTeam");

        mockMvc.perform(put("/teams")
                .content(objectMapper.writeValueAsString(clientTeamDTO))
                .contentType("application/json")
                .session(session))
                .andExpect(status().isOk());

        List<ClientTeamDTO> clientTeamDTOS = getClientTeamDTOS();

        assertThat(clientTeamDTOS.stream()
                .map(ClientTeamDTO::getName)
                .anyMatch(u -> u.equals("EditedClientTeam")))
                .isTrue();
    }

    private List<ClientTeamDTO> getClientTeamDTOS() throws Exception {
        setupAuth("nonAdminUser@mirado.com");

        String clientTeamDTOSAsString = mockMvc.perform(get("/teams")
                .session(session))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ClientTeamDTO[] clientTeamDTOS = objectMapper.readValue(clientTeamDTOSAsString, ClientTeamDTO[].class);

        return List.of(clientTeamDTOS);
    }
}