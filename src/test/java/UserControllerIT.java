import com.consultant.model.dto.UserDTO;
import com.consultant.model.dto.VacationDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerIT extends AbstractControllerIT {

    @Test
    public void testUserDeletion() throws Exception {
        setupAuth(ADMIN_EMAIL);
        Long userId = 3L;

        List<UserDTO> usersBefore = getUserDTOS();

        mockMvc.perform(delete("/user/" + userId)
                .session(session))
                .andExpect(status().isOk());

        List<UserDTO> usersAfter = getUserDTOS();

        String vacationsAsString = mockMvc.perform(get("/vacations")
                .session(session))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        VacationDTO[] vacationDTOS = objectMapper.readValue(vacationsAsString, VacationDTO[].class);
        List<VacationDTO> vacations = List.of(vacationDTOS);

        assertThat(vacations.stream().anyMatch(vacation -> vacation.getUserId().equals(userId))).isFalse();
        assertThat(usersAfter.size()).isEqualTo(usersBefore.size() - 1);
    }

    @Test
    public void testUserRetrieving() throws Exception {
        setupAuth(NON_ADMIN_EMAIL);

        String usersAsString = mockMvc.perform(get("/user")
                .session(session))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        UserDTO[] userDTOS = objectMapper.readValue(usersAsString, UserDTO[].class);

        List<UserDTO> users = List.of(userDTOS);

        assertThat(users.stream().anyMatch(user -> user.getEmail().equals(ADMIN_EMAIL))).isTrue();
    }

    @Test
    public void testThatNonAdminUserHasNoAccess() throws Exception {
        setupAuth(NON_ADMIN_EMAIL);

        mockMvc.perform(delete("/user/3")
                .session(session))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testThatNonAuthenticatedUserHasNoAccess() throws Exception {
        mockMvc.perform(get("/user"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testUserEditing() throws Exception {
        setupAuth(ADMIN_EMAIL);

        List<UserDTO> usersBefore = getUserDTOS();

        List<String> roles = List.of("user", "admin");

        UserDTO editedUser = new UserDTO();
        editedUser.setId(2L);
        editedUser.setEmail("userToEdit@mirado.com");
        editedUser.setRoles(roles);

        mockMvc.perform(put("/user")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(editedUser))
                .session(session))
                .andExpect(status().isOk());

        List<UserDTO> usersAfter = getUserDTOS();

        assertThat(usersBefore.stream()
                .filter(user -> user.getEmail().equals("userToEdit@mirado.com") && user.getRoles().contains("admin"))
                .count())
                .as("userToEdit did not have admin role before edit")
                .isEqualTo(0);

        assertThat(usersAfter.stream()
                .filter(user -> user.getEmail().equals("userToEdit@mirado.com") && user.getRoles().contains("admin"))
                .count())
                .as("userToEdit does have admin role after edit")
                .isEqualTo(1);
    }

    private List<UserDTO> getUserDTOS() throws Exception {
        setupAuth("admin@mirado.com");

        String responseBodyAsString = mockMvc.perform(get("/user")
                .session(session))
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDTO[] users = objectMapper.readValue(responseBodyAsString, UserDTO[].class);

        return List.of(users);
    }
}