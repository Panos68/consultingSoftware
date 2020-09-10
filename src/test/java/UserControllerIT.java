import com.consultant.model.dto.UserDTO;
import com.consultant.model.dto.VacationDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles(profiles = "test")
@ContextConfiguration(classes = ConsultancyManagementApplication.class)
@SpringBootTest(classes = {ConsultancyManagementApplication.class})
@AutoConfigureMockMvc
public class UserControllerIT {

//    @Autowired
//    ObjectMapper objectMapper;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
////    @WithMockUser(authorities = {"admin"}, username = "user@mirado.com")
//    public void testUserDeletion() throws Exception {
//
//        Principal principal = mock(Principal.class);
//        when(principal.getName()).thenReturn("user");
//
//        OAuth2AuthenticationToken token = mock(OAuth2AuthenticationToken.class);
//
//        when(token.getPrincipal().getAttribute("email")).thenReturn("admin@mirado.com");
//
//
////        final var storedRequest = mock(OAuth2Request.class);
//
//
//        final var userAuthentication = mock(Authentication.class);
//        when(userAuthentication.getAuthorities()).thenReturn(Set.of(new SimpleGrantedAuthority("admin")));
//        when(userAuthentication.getPrincipal()).thenReturn(principal);
//
//         OAuth2Authentication oauth2Authentication = new OAuth2Authentication(storedRequest, authentication);
//
//        SecurityContextHolder.getContext().setAuthentication(oauth2Authentication);
//
//
//
//        Long userId = 3L;
//
//        List<UserDTO> usersBefore = getUserDTOS();
//
//        mockMvc.perform(delete("/user/" + userId))
//                .andExpect(status().isOk());
//
//        List<UserDTO> usersAfter = getUserDTOS();
//
//        String vacationsAsString = mockMvc.perform(get("/vacations"))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//
//        VacationDTO[] vacationDTOS = objectMapper.readValue(vacationsAsString, VacationDTO[].class);
//        List<VacationDTO> vacations = List.of(vacationDTOS);
//
//        assertThat(vacations.stream().anyMatch(vacation -> vacation.getUserId().equals(userId))).isFalse();
//        assertThat(usersAfter.size()).isEqualTo(usersBefore.size() - 1);
//    }
//
//    @Test
//    @WithMockUser(authorities = {"admin"}, username = "admin@mirado.com")
//    public void testUserRetrieving() throws Exception {
//
//        String usersAsString = mockMvc.perform(get("/user"))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//
//        UserDTO[] userDTOS = objectMapper.readValue(usersAsString, UserDTO[].class);
//
//        List<UserDTO> users = List.of(userDTOS);
//
//        assertThat(users.stream().anyMatch(user -> user.getEmail().equals("admin@mirado.com"))).isTrue();
//    }
//
//    @Test
//    @WithMockUser(authorities = {"user"}, username = "user@mirado.com")
//    public void testThatNonAdminUserHasNoAccess() throws Exception {
//
//        mockMvc.perform(delete("/user/1"))
//                .andExpect(status().isForbidden());
//    }
////
////    @Test
////    public void testUserEditing() throws Exception {
////        addAuthorizationRequestToHeader();
////
////        UserDTO editedUser = new UserDTO();
////        editedUser.setId(2L);
////        editedUser.setUsername("EditedUser");
////        editedUser.setPassword("123");
////
////        HttpEntity<UserDTO> entity = new HttpEntity<>(editedUser, headers);
////        ResponseEntity<String> editUserResponse = restTemplate.exchange(
////                createURLWithPort("/user"), HttpMethod.PUT, entity, String.class);
////
////        List<UserDTO> userList = getUserDTOS();
////
////        assertTrue(Objects.requireNonNull(userList).stream().map(UserDTO::getUsername).anyMatch(u -> u.equals("EditedUser")));
////        assertEquals(editUserResponse.getStatusCode(), HttpStatus.OK);
////    }
//
//    private List<UserDTO> getUserDTOS() throws Exception {
//
//        String responseBodyAsString = mockMvc.perform(get("/user"))
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        UserDTO[] users = objectMapper.readValue(responseBodyAsString, UserDTO[].class);
//
//        return List.of(users);
//    }
}