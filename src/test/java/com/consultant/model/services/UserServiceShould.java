package com.consultant.model.services;

import com.consultant.model.dto.UserDTO;
import com.consultant.model.entities.User;
import com.consultant.model.entities.Vacation;
import com.consultant.model.exception.EntityAlreadyExists;
import com.consultant.model.exception.NoMatchException;
import com.consultant.model.repositories.UserRepository;
import com.consultant.model.services.impl.ConsultantService;
import com.consultant.model.services.impl.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceShould {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ConsultantService consultantService;

    private UserService userService;

    private Long userId = 1L;

    private String userEmail = "user@mirado.com";

    private User user1 = new User();

    private User user2 = new User();

    private List<User> userList = new ArrayList<>();

    private UserDTO userDTO = new UserDTO();

    private Vacation vacation = new Vacation();

    @Before
    public void setUp() {
        userService = new UserService(userRepository, consultantService);

        user1 = new User();
        user1.setId(userId);
        user1.setEmail(userEmail);
        when(userRepository.findAll()).thenReturn(userList);

        userDTO = new UserDTO();
    }

    @Test
    public void returnListOfAllExistingUsers() {
        userList.add(user1);
        userList.add(user2);

        Set<UserDTO> userDTOS = userService.getAll();

        Assert.assertThat(userDTOS.size(), is(2));
    }

    @Test
    public void returnEmptyListIfThereAreNoUsers() {
        when(userRepository.findAll()).thenReturn(userList);

        Set<UserDTO> userDTOS = userService.getAll();

        Assert.assertThat(userDTOS.isEmpty(), is(true));
    }

    @Test
    public void deleteInRepositoryWhenDeletingExistingUser() throws NoMatchException {
        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(user1));

        userService.delete(userId);

        verify(userRepository, times(1)).delete(user1);
    }

    @Test(expected = NoMatchException.class)
    public void throwExceptionWhenDeletingNonExistingUser() throws NoMatchException {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        userService.delete(userId);
    }

    @Test
    public void updateOnEditingExistingUser() throws NoMatchException {
        List<String> roles = new ArrayList<>();
        roles.add("admin");

        userDTO.setRoles(roles);
        userDTO.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(user1));

        userService.edit(userDTO);

        verify(userRepository, times(1)).saveAndFlush(user1);
    }

    @Test(expected = NoMatchException.class)
    public void throwExceptionWhenUpdatingNonExistingUser() throws NoMatchException {
        List<String> roles = new ArrayList<>();
        roles.add("admin");

        userDTO.setRoles(roles);
        userDTO.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        userService.edit(userDTO);
    }

    @Test
    public void saveToRepositoryWhenUpdatingVacations() throws NoMatchException {
        vacation.setUserId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(user1));

        userService.updateUserVacations(vacation);

        verify(userRepository, times(1)).saveAndFlush(user1);
    }

    @Test(expected = NoMatchException.class)
    public void throwExceptionWhenUpdatingVacationsOfNonExistingUser() throws NoMatchException {
        vacation.setUserId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        userService.updateUserVacations(vacation);
    }

    @Test
    public void returnUserWhenSearchingVacation() throws NoMatchException {
        Long vacationId = 1L;
        vacation.setUserId(userId);
        vacation.setId(vacationId);
        when(userRepository.findByVacationId(vacationId)).thenReturn(Optional.ofNullable(user1));

        User user = userService.getUserByVacationId(vacationId);

        Assert.assertEquals(user1, user);
    }

    @Test(expected = NoMatchException.class)
    public void throwExceptionWhenSearchingOfVacationOfNonExistingUser() throws NoMatchException {
        Long vacationId = 1L;
        vacation.setUserId(userId);
        vacation.setId(vacationId);
        when(userRepository.findByVacationId(vacationId)).thenReturn(Optional.empty());

        userService.getUserByVacationId(vacationId);
    }
}