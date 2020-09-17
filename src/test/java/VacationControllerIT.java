import com.consultant.model.dto.UserDTO;
import com.consultant.model.dto.VacationDTO;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VacationControllerIT extends AbstractControllerIT {

    @Test
    public void testVacationRetrieving() throws Exception {
        setupAuth(NON_ADMIN_EMAIL);

        String responseBodyAsString = mockMvc.perform(get("/vacations")
                .session(session))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        VacationDTO[] vacationDTOS = objectMapper.readValue(responseBodyAsString, VacationDTO[].class);
        List<VacationDTO> vacations = List.of(vacationDTOS);

        assertThat(vacations.stream()
                .map(VacationDTO::getDescription)
                .anyMatch(u -> u.equals("Brazil")))
                .isTrue();
    }

    @Test
    public void testVacationRetrievingForSpecificUser() throws Exception {
        setupAuth(NON_ADMIN_EMAIL);

        String responseBodyAsString = mockMvc.perform(get("/vacations/user/1")
                .session(session))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        VacationDTO[] vacationDTOS = objectMapper.readValue(responseBodyAsString, VacationDTO[].class);
        List<VacationDTO> vacations = List.of(vacationDTOS);

        assertThat(vacations.size()).isEqualTo(2);
    }

    @Test
    public void testVacationCreation() throws Exception {
        VacationDTO vacationDTO = new VacationDTO();
        vacationDTO.setDescription("Created");
        vacationDTO.setUserId(2L);

        setupAuth(NON_ADMIN_EMAIL);

        mockMvc.perform(post("/vacations")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(vacationDTO))
                .session(session))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<VacationDTO> vacations = getVacationsDTOList();

        String usersAsString = mockMvc.perform(get("/user")
                .session(session))
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDTO[] userDTOS = objectMapper.readValue(usersAsString, UserDTO[].class);
        List<UserDTO> users = List.of(userDTOS);

        boolean vacationExistsForUser = users.stream()
                .filter(userDTO -> userDTO.getId() == 2)
                .map(UserDTO::getVacations)
                .flatMap(Collection::stream)
                .anyMatch(vacation -> vacation.getDescription().equals("Created"));

        assertThat(vacationExistsForUser).isTrue();
        assertThat(vacations.stream()
                .map(VacationDTO::getDescription)
                .anyMatch(u -> u.equals("Created")))
                .isTrue();
    }

    @Test
    public void testVacationDeletion() throws Exception {
        setupAuth(NON_ADMIN_EMAIL);

        mockMvc.perform(delete("/vacations/3")
                .session(session))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<VacationDTO> vacations = getVacationsDTOList();

        assertThat(vacations.stream()
                .map(VacationDTO::getDescription)
                .anyMatch(u -> u.equals("DeleteVac")))
                .isFalse();
    }

    @Test
    public void testVacationEditing() throws Exception {
        setupAuth(NON_ADMIN_EMAIL);

        VacationDTO editedVacation = new VacationDTO();
        editedVacation.setId(2L);
        editedVacation.setDescription("EditedVacation");

        mockMvc.perform(put("/vacations")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(editedVacation))
                .session(session))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<VacationDTO> vacations = getVacationsDTOList();

        assertThat(vacations.stream()
                .map(VacationDTO::getDescription)
                .anyMatch(u -> u.equals("EditedVacation")))
                .isTrue();
    }

    private List<VacationDTO> getVacationsDTOList() throws Exception {
        setupAuth(NON_ADMIN_EMAIL);

        String vacationsAsString = mockMvc.perform(get("/vacations")
                .session(session))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        VacationDTO[] vacationDTOS = objectMapper.readValue(vacationsAsString, VacationDTO[].class);
        return List.of(vacationDTOS);
    }
}