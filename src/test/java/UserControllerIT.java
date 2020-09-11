import com.consultant.model.dto.UserDTO;
import com.consultant.model.dto.VacationDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles(profiles = "test")
@ContextConfiguration(classes = ConsultancyManagementApplication.class)
@SpringBootTest(classes = {ConsultancyManagementApplication.class})
@AutoConfigureMockMvc
public class UserControllerIT {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private static final String ADMIN_EMAIL = "admin@mirado.com";
    private static final String NON_ADMIN_EMAIL = "nonAdminUser@mirado.com";

    private MockHttpSession session;

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

    // TODO [aw] check which exception
    @Test
    public void testThatNonAdminUserHasNoAccess() throws Exception {
        setupAuth(NON_ADMIN_EMAIL);

        assertThatThrownBy(() -> mockMvc.perform(delete("/user/3")
                .session(session)));
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

    void setupAuth(String email) {
        OAuth2AuthenticationToken oauthToken = buildPrincipal(email);
        session = new MockHttpSession();
        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                new SecurityContextImpl(oauthToken));
    }

    private static OAuth2AuthenticationToken buildPrincipal(String email) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sub", "my-id");
        attributes.put("email", email);

        List<GrantedAuthority> authorities = Collections.singletonList(
                new OAuth2UserAuthority("user", attributes));
        OAuth2User user = new DefaultOAuth2User(authorities, attributes, "sub");
        return new OAuth2AuthenticationToken(user, authorities, "whatever");
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