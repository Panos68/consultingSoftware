import com.consultant.model.dto.UserDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
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

import java.time.LocalDate;
import java.util.Collections;

@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = "test")
@ContextConfiguration(classes = ConsultancyManagementApplication.class)
@SpringBootTest(classes = {ConsultancyManagementApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractControllerIT {
    @LocalServerPort
    private int port;

    SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();

    RestTemplate restTemplate = new RestTemplate(requestFactory);

    HttpHeaders headers = new HttpHeaders();

    UserDTO userDTO = new UserDTO();

    final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class,
            (JsonDeserializer<LocalDate>)
                    (jsonElement, type, jsonDeserializationContext) -> LocalDate.parse(jsonElement.getAsJsonPrimitive().getAsString())).create();

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

    void addAuthorizationRequestToHeader() throws JSONException {
        JSONObject jsonObject = getUserAuthenticationResponseAsJson();
        String jwtToken = String.valueOf(jsonObject.get("jwtToken"));
        headers.add("Authorization", "Bearer " + jwtToken);
    }

    JSONObject getUserAuthenticationResponseAsJson() throws JSONException {
        HttpEntity<UserDTO> entity = new HttpEntity<>(userDTO, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/user/authenticate"), HttpMethod.POST, entity, String.class);

        return new JSONObject(response.getBody());
    }
}
