import com.consultant.model.dto.UserDTO;
import com.consultant.model.dto.VacationDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class VacationControllerIT extends AbstractControllerIT {

    Gson gson = new Gson();

    @Test
    public void testVacationRetrieving() throws Exception {
        addAuthorizationRequestToHeader();

        HttpEntity<VacationDTO> getVacationsEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/vacations"), HttpMethod.GET, getVacationsEntity, String.class);
        Type type = new TypeToken<List<VacationDTO>>() {}.getType();

        List<VacationDTO> vacationsList = gson.fromJson(response.getBody(), type);

        assertTrue(Objects.requireNonNull(vacationsList).stream().map(VacationDTO::getDescription).anyMatch(u -> u.equals("Brazil")));
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testVacationRetrievingForSpecificUser() throws Exception {
        addAuthorizationRequestToHeader();

        HttpEntity<VacationDTO> getVacationsEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/vacations/user/1"), HttpMethod.GET, getVacationsEntity, String.class);
        Type type = new TypeToken<List<VacationDTO>>() {}.getType();

        List<VacationDTO> vacationsList = gson.fromJson(response.getBody(), type);

        assertEquals(3, vacationsList.size());
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }


    @Test
    public void testVacationCreation() throws Exception {
        VacationDTO vacationDTO = new VacationDTO();
        vacationDTO.setDescription("Created");
        vacationDTO.setUserId(1L);

        addAuthorizationRequestToHeader();

        HttpEntity<VacationDTO> entity = new HttpEntity<>(vacationDTO, headers);
        ResponseEntity<String> createVacationResponse = restTemplate.exchange(
                createURLWithPort("/vacations"), HttpMethod.POST, entity, String.class);

        List<VacationDTO> vacationsList = getVacationsDTOList();

        HttpEntity<UserDTO> getUsersEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> userResponse = restTemplate.exchange(
                createURLWithPort("/user"), HttpMethod.GET, getUsersEntity, String.class);
        Type userType = new TypeToken<List<UserDTO>>() {}.getType();

        List<UserDTO> userDTOList = gson.fromJson(userResponse.getBody(), userType);
        boolean consultantExistsForTeam = userDTOList.stream()
                .filter(userDTO -> userDTO.getId() == 1).map(UserDTO::getVacations)
                .flatMap(Collection::stream)
                .anyMatch(vacation -> vacation.getDescription().equals("Created"));

        assertTrue(consultantExistsForTeam);
        assertTrue(Objects.requireNonNull(vacationsList).stream().map(VacationDTO::getDescription).anyMatch(u -> u.equals("Created")));
        assertEquals(createVacationResponse.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testVacationDeletion() throws Exception {
        addAuthorizationRequestToHeader();

        HttpEntity<VacationDTO> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> deleteVacationResponse = restTemplate.exchange(
                createURLWithPort("/vacations/3"), HttpMethod.DELETE, entity, String.class);

        List<VacationDTO> vacationsList = getVacationsDTOList();

        assertFalse(Objects.requireNonNull(vacationsList).stream().map(VacationDTO::getDescription).anyMatch(u -> u.equals("DeleteVac")));
        assertEquals(deleteVacationResponse.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testVacationEditing() throws Exception {
        addAuthorizationRequestToHeader();

        VacationDTO editedVacation = new VacationDTO();
        editedVacation.setId(2L);
        editedVacation.setDescription("EditedVacation");

        HttpEntity<VacationDTO> entity = new HttpEntity<>(editedVacation, headers);
        ResponseEntity<String> editVacationResponse = restTemplate.exchange(
                createURLWithPort("/vacations"), HttpMethod.PUT, entity, String.class);

        List<VacationDTO> vacationsList = getVacationsDTOList();

        assertTrue(Objects.requireNonNull(vacationsList).stream().map(VacationDTO::getDescription).anyMatch(u -> u.equals("EditedVacation")));
        assertEquals(editVacationResponse.getStatusCode(), HttpStatus.OK);
    }

    private List<VacationDTO> getVacationsDTOList() {
        HttpEntity<VacationDTO> getVacationsEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/vacations"), HttpMethod.GET, getVacationsEntity, String.class);

        Type type = new TypeToken<List<VacationDTO>>() {}.getType();
        return gson.fromJson(response.getBody(), type);
    }
}