import com.consultant.model.dto.UserDTO;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = "test")
@ContextConfiguration(classes = MiradoInternalApplication.class)
@SpringBootTest(classes = {MiradoInternalApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class ITtests {
    @LocalServerPort
    private int port;

    SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
    RestTemplate restTemplate = new RestTemplate(requestFactory);
    HttpHeaders headers = new HttpHeaders();

    UserDTO userDTO = new UserDTO();

    @Before
    public void setUp() {
        userDTO.setUsername("Admin");
        userDTO.setPassword("123");
        userDTO.setRoles(Collections.singletonList("admin"));
        requestFactory.setOutputStreaming(false);
    }

    String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

     String getToken() throws JSONException {
        JSONObject jsonObject = getUserAuthenticationResponseAsJson();
        return String.valueOf(jsonObject.get("jwtToken"));
    }

    JSONObject getUserAuthenticationResponseAsJson() throws JSONException {
        HttpEntity<UserDTO> entity = new HttpEntity<>(userDTO, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/user/authenticate"), HttpMethod.POST, entity, String.class);

        return new JSONObject(response.getBody());
    }
}
