import com.consultant.model.dto.ClientTeamDTO;
import com.consultant.model.dto.ConsultantDTO;
import com.consultant.model.dto.ContractDTO;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ConsultantControllerIT extends AbstractControllerIT {

    @Test
    public void testConsultantsRetrieving() throws Exception {
        setupAuth(NON_ADMIN_EMAIL);

        String responseBodyAsString = mockMvc.perform(get("/consultants")
                .session(session))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ConsultantDTO[] consultantDTOS = objectMapper.readValue(responseBodyAsString, ConsultantDTO[].class);

        assertThat(List.of(consultantDTOS)
                .stream()
                .map(ConsultantDTO::getId)
                .anyMatch(u -> u.equals(1L)))
                .isTrue();
    }


    @Test
    public void testConsultantCreation() throws Exception {
        ConsultantDTO consultantDTO = new ConsultantDTO();
        consultantDTO.setFirstName("Created");
        consultantDTO.setTeamId(1L);
        consultantDTO.setActiveContract(new ContractDTO());

        setupAuth(NON_ADMIN_EMAIL);

        mockMvc.perform(post("/consultants")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(consultantDTO))
                .session(session))
                .andExpect(status().isOk());

        String consultantsAsString = mockMvc.perform(get("/consultants")
                .session(session))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ConsultantDTO[] consultantDTOS = objectMapper.readValue(consultantsAsString, ConsultantDTO[].class);

        String teamsAsString = mockMvc.perform(get("/teams")
                .session(session))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ClientTeamDTO[] clientTeamDTOS = objectMapper.readValue(teamsAsString, ClientTeamDTO[].class);

        boolean consultantExistsForTeam = List.of(clientTeamDTOS)
                .stream()
                .filter(clientTeamDTO -> clientTeamDTO.getId() == 1).map(ClientTeamDTO::getConsultants)
                .flatMap(Collection::stream)
                .anyMatch(clientTeam -> clientTeam.getFirstName().equals("Created"));

        assertThat(consultantExistsForTeam).isTrue();
        assertThat(List.of(consultantDTOS)
                .stream()
                .map(ConsultantDTO::getFirstName)
                .anyMatch(u -> u.equals("Created")))
                .isTrue();
    }

    @Test
    public void testConsultantDeletion() throws Exception {
        setupAuth(NON_ADMIN_EMAIL);

        mockMvc.perform(delete("/consultants/3/?terminatedDate=2020-01-01")
                .session(session))
                .andExpect(status().isOk());

        String consultantsAsString = mockMvc.perform(get("/consultants")
                .session(session))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ConsultantDTO[] consultantDTOS = objectMapper.readValue(consultantsAsString, ConsultantDTO[].class);

        Optional<ConsultantDTO> deletedConsultant = List.of(consultantDTOS).stream()
                .filter(ConsultantDTO::getDeleted)
                .findFirst();

        assertThat(deletedConsultant).isPresent();
        assertThat(deletedConsultant.get()
                .getContracts()
                .stream()
                .anyMatch(ContractDTO::getActive))
                .isFalse();
    }

    @Test
    public void testConsultantEditing() throws Exception {
        setupAuth(NON_ADMIN_EMAIL);

        ConsultantDTO consultantDTO = new ConsultantDTO();
        consultantDTO.setId(2L);
        consultantDTO.setFirstName("EditedConsultant");
        consultantDTO.setActiveContract(new ContractDTO());

        mockMvc.perform(put("/consultants")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(consultantDTO))
                .session(session))
                .andExpect(status().isOk());

        String consultantsAsString = mockMvc.perform(get("/consultants")
                .session(session))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ConsultantDTO[] consultantDTOS = objectMapper.readValue(consultantsAsString, ConsultantDTO[].class);

        assertThat(List.of(consultantDTOS)
                .stream()
                .map(ConsultantDTO::getFirstName)
                .anyMatch(u -> u.equals("EditedConsultant")))
                .isTrue();
    }
}