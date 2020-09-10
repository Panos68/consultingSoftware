import com.consultant.model.dto.UserDTO;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = "test")
@ContextConfiguration(classes = ConsultancyManagementApplication.class)
@SpringBootTest(classes = {ConsultancyManagementApplication.class}
//, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)


public abstract class AbstractControllerIT {

    private MockMvc mockMvc;

//    @LocalServerPort
//    private int port;

    SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();

    RestTemplate restTemplate = new RestTemplate(requestFactory);

    HttpHeaders headers = new HttpHeaders();

    UserDTO userDTO = new UserDTO();

    final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class,
            (JsonDeserializer<LocalDate>)
                    (jsonElement, type, jsonDeserializationContext) -> LocalDate.parse(jsonElement.getAsJsonPrimitive().getAsString())).create();

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Before
    public void setUp() {
        userDTO.setEmail("admin@mirado.com");
        userDTO.setRoles(Collections.singletonList("admin"));
        requestFactory.setOutputStreaming(false);

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilter(springSecurityFilterChain).build();
    }

//    private String obtainAccessToken(String username, String password) throws Exception {
//
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//
//        Map<String, StringValuePattern> par = new HashMap<>();
//        par.put("grant_type", WireMock.equalTo("password"));
//        par.put("client_id", WireMock.equalTo("fooClientIdPassword"));
//        par.put("username", WireMock.equalTo(username));
//        par.put("password", WireMock.equalTo(password));
//
//        ResultActions result
//                = mockMvc.perform(post("/oauth/token")
//                .withQueryParams(par)
//                .withBasicAuth("fooClientIdPassword","secret")
//                .accept("application/json;charset=UTF-8"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"));
//
//        String resultString = result.andReturn().getResponse().getContentAsString();
//
//        JacksonJsonParser jsonParser = new JacksonJsonParser();
//        return jsonParser.parseMap(resultString).get("access_token").toString();
//    }


//    String createURLWithPort(String uri) {
//        return "http://localhost:" + "8080" + uri;
//    }

//    @Before
//    public void setup() {
//
//        // set up a Mock OAuth server
//        stubFor(get(urlPathMatching("/oauth/authorize.*"))
//                .willReturn(aResponse()
//                        .withStatus(302)
//                        .withHeader("Location", "http://localhost:8080/login/oauth2/code/google?code=my-acccess-code&state=${state}")
//                        .withTransformers("CaptureStateTransformer")
//                )
//        );
//
//        stubFor(post(urlPathMatching("/oauth/token"))
//                .willReturn(aResponse()
//                        .withStatus(200)
//                        .withHeader("Content-Type", "application/json")
//                        .withBody("{\"access_token\":\"my-access-token\"" +
//                                ", \"token_type\":\"Bearer\"" +
//                                ", \"expires_in\":\"3600\"" +
//                                "}")
//                )
//        );
//
//        stubFor(get(urlPathMatching("/userinfo"))
//                .willReturn(aResponse()
//                        .withStatus(200)
//                        .withHeader("Content-Type", "application/json")
//                        .withBody("{\"sub\":\"1\"" +
//                                ",\"name\":\"Mark Hoogenboom\"" +
//                                ", \"email\":\"admin@mirado.com\"" +
//                                "}")
//                )
//        );

//        webClient = MockMvcWebClientBuilder
//                .webAppContextSetup(context, springSecurity())
//                .build();
//    }

//    void addAuthorizationRequestToHeader() throws JSONException {
//        JSONObject jsonObject = getUserAuthenticationResponseAsJson();
//        String jwtToken = String.valueOf(jsonObject.get("jwtToken"));
//        headers.add("Authorization", "Bearer " + jwtToken);
//    }

//    JSONObject getUserAuthenticationResponseAsJson() throws JSONException {
//        HttpEntity<UserDTO> entity = new HttpEntity<>(userDTO, headers);
//        ResponseEntity<String> response = restTemplate.exchange(
//                createURLWithPort("/user/authenticate"), HttpMethod.POST, entity, String.class);
//
//        return new JSONObject(response.getBody());
//    }
}
