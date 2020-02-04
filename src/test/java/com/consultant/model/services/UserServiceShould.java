package com.consultant.model.services;

import com.consultant.model.dto.UserDTO;
import com.consultant.model.entities.User;
import com.consultant.model.exception.EntityAlreadyExists;
import com.consultant.model.exception.NoMatchException;
import com.consultant.model.repositories.UserRepository;
import com.consultant.model.services.impl.UserServiceImpl;
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

    private UserService userService;

    private Long userId = 1L;

    private User user1 = new User();

    private User user2 = new User();

    private List<User> userList = new ArrayList<>();

    private UserDTO userDTO = new UserDTO();

    @Before
    public void setUp() {
        userService = new UserServiceImpl(userRepository);

        user1 = new User();
        user1.setId(userId);
        when(userRepository.findAll()).thenReturn(userList);

        userDTO = new UserDTO();
    }

    @Test
    public void returnListOfAllExistingUsers() {
        userList.add(user1);
        userList.add(user2);
        Set<UserDTO> userDTOS = userService.getAllUsers();
        Assert.assertThat(userDTOS.size(), is(2));
    }

    @Test
    public void returnEmptyListIfThereAreNoUsers() {
        when(userRepository.findAll()).thenReturn(userList);
        Set<UserDTO> userDTOS = userService.getAllUsers();

        Assert.assertThat(userDTOS.isEmpty(), is(true));
    }

    @Test
    public void saveToRepositoryWhenCreatingUser() {
        userDTO = new UserDTO();
        userDTO.setId(userId);
        userService.createUser(userDTO);

        verify(userRepository, times(1)).saveAndFlush(user1);
    }

    @Test(expected = EntityAlreadyExists.class)
    public void throwExceptionWhenCreatingUserWithSameUsername() throws EntityAlreadyExists {
        String username = "username";
        user1.setUsername(username);
        userDTO.setUsername(username);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.ofNullable(user1));
        userService.createUser(userDTO);
    }

    @Test
    public void deleteInRepositoryWhenDeletingExistingUser() throws NoMatchException {
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(user1));
        userService.deleteUser(userId);

        verify(userRepository, times(1)).delete(user1);
    }

    @Test(expected = NoMatchException.class)
    public void throwExceptionWhenDeletingNonExistingCandidate() throws NoMatchException {
        userService.deleteUser(userId);
    }

    @Test
    public void updateOnEditingExistingCandidate() throws NoMatchException {
        userDTO.setPassword("updated");
        userDTO.setId(userId);
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(user1));
        userService.editUser(userDTO);

        verify(userRepository, times(1)).saveAndFlush(user1);
    }

    @Test(expected = NoMatchException.class)
    public void throwExceptionWhenUpdatingNonExistingCandidate() throws NoMatchException {
        userDTO.setPassword("updated");
        userDTO.setId(userId);
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());
        userService.editUser(userDTO);
    }
}