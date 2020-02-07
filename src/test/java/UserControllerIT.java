import com.consultant.model.dto.UserDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerIT extends ITtests {

    TestRestTemplate restTemplate = new TestRestTemplate();
    HttpHeaders headers = new HttpHeaders();

    Gson gson = new Gson();

    @Test
    public void testUserAuthentication() throws Exception {
        JSONObject jsonObject = getUserAuthenticationResponseAsJson();
        String jwtToken = String.valueOf(jsonObject.get("jwtToken"));
        String admin = String.valueOf(jsonObject.getJSONArray("roles").getJSONObject(0).get("authority"));

        assertFalse(jwtToken.isEmpty());
        assertEquals("admin", admin);
    }

    @Test
    public void testNonExistingUserAuthentication() {
        RestTemplate restTemplate = new RestTemplate(requestFactory);

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("NonExistingUser");
        userDTO.setPassword("123");
        HttpEntity<UserDTO> entity = new HttpEntity<>(userDTO, headers);
        try {
            restTemplate.exchange(createURLWithPort("/user/authenticate"), HttpMethod.POST, entity, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            assertEquals(HttpStatus.UNAUTHORIZED, ((HttpClientErrorException.Unauthorized) e).getStatusCode());
        }
    }

    @Test
    public void testWrongCredentialsAuthentication() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("Admin");
        userDTO.setPassword("1234");
        HttpEntity<UserDTO> entity = new HttpEntity<>(userDTO, headers);
        try {
            restTemplate.exchange(createURLWithPort("/user/authenticate"), HttpMethod.POST, entity, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            assertEquals(HttpStatus.UNAUTHORIZED, ((HttpClientErrorException.Unauthorized) e).getStatusCode());
        }
    }

    @Test
    public void testUserRetrieving() throws Exception {
        String jwtToken = getToken();

        headers.add("Authorization", "Bearer " + jwtToken);


        HttpEntity<UserDTO> getUsersEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/user"), HttpMethod.GET, getUsersEntity, String.class);
        Type type = new TypeToken<List<UserDTO>>() {}.getType();

        List<UserDTO> userList = gson.fromJson(response.getBody(), type);

        assertTrue(Objects.requireNonNull(userList).stream().map(UserDTO::getUsername).anyMatch(u -> u.equals("Admin")));
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }


    @Test
    public void testUserCreation() throws Exception {
        String jwtToken = getToken();

        UserDTO userDTO2 = new UserDTO();
        userDTO2.setUsername("Test");
        userDTO2.setPassword("123");

        headers.add("Authorization", "Bearer " + jwtToken);

        HttpEntity<UserDTO> entity = new HttpEntity<>(userDTO2, headers);
        ResponseEntity<String> createUserResponse = restTemplate.exchange(
                createURLWithPort("/user"), HttpMethod.POST, entity, String.class);

        HttpEntity<UserDTO> getUsersEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/user"), HttpMethod.GET, getUsersEntity, String.class);
        Type type = new TypeToken<List<UserDTO>>() {
        }.getType();

        List<UserDTO> userList = gson.fromJson(response.getBody(), type);

        assertTrue(Objects.requireNonNull(userList).stream().map(UserDTO::getUsername).anyMatch(u -> u.equals("Test")));
        assertEquals(createUserResponse.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testUserDeletion() throws Exception {
        String jwtToken = getToken();

        headers.add("Authorization", "Bearer " + jwtToken);

        HttpEntity<UserDTO> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> deleteUserResponse = restTemplate.exchange(
                createURLWithPort("/user/3"), HttpMethod.DELETE, entity, String.class);

        HttpEntity<UserDTO> getUsersEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/user"), HttpMethod.GET, getUsersEntity, String.class);
        Type type = new TypeToken<List<UserDTO>>() {}.getType();

        List<UserDTO> userList = gson.fromJson(response.getBody(), type);

        assertFalse(Objects.requireNonNull(userList).stream().map(UserDTO::getUsername).anyMatch(u -> u.equals("UserToDelete")));
        assertEquals(deleteUserResponse.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testUserEditing() throws Exception {
        String jwtToken = getToken();

        headers.add("Authorization", "Bearer " + jwtToken);

        UserDTO editedUser = new UserDTO();
        editedUser.setId(2L);
        editedUser.setUsername("EditedUser");
        editedUser.setPassword("123");
        HttpEntity<UserDTO> entity = new HttpEntity<>(editedUser, headers);
        ResponseEntity<String> deleteUserResponse = restTemplate.exchange(
                createURLWithPort("/user"), HttpMethod.PUT, entity, String.class);

        HttpEntity<UserDTO> getUsersEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/user"), HttpMethod.GET, getUsersEntity, String.class);
        Type type = new TypeToken<List<UserDTO>>() {}.getType();

        List<UserDTO> userList = gson.fromJson(response.getBody(), type);

        assertTrue(Objects.requireNonNull(userList).stream().map(UserDTO::getUsername).anyMatch(u -> u.equals("EditedUser")));
        assertEquals(deleteUserResponse.getStatusCode(), HttpStatus.OK);
    }
}