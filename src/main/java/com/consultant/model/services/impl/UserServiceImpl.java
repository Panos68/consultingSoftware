package com.consultant.model.services.impl;

import com.consultant.model.dto.UserDTO;
import com.consultant.model.entities.User;
import com.consultant.model.entities.Vacation;
import com.consultant.model.exception.EntityAlreadyExists;
import com.consultant.model.exception.NoMatchException;
import com.consultant.model.mappers.UserMapper;
import com.consultant.model.repositories.UserRepository;
import com.consultant.model.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Set<UserDTO> getAllUsers() {
        List<User> usersList = userRepository.findAll();
        Set<UserDTO> userDTOS = new HashSet<>();
        usersList.forEach(user -> {
            final UserDTO userDTO = UserMapper.INSTANCE.userToUserDTO(user);
            userDTOS.add(userDTO);
        });

        return userDTOS;
    }

    @Override
    public void createUser(UserDTO userDTO) {
        Optional<User> existingUser = userRepository.findByUsername(userDTO.getUsername());
        if (existingUser.isPresent()) {
            throw new EntityAlreadyExists("User already exists");
        }
        final User user = UserMapper.INSTANCE.userDTOToUser(userDTO);

        userRepository.saveAndFlush(user);
    }

    @Override
    public void editUser(UserDTO userDTO) throws NoMatchException {
        User existingUser = getExistingUserById(userDTO.getId());

        updateUser(existingUser, userDTO);
    }

    private void updateUser(User existingUser, UserDTO userDTO) {
        existingUser.setPassword(userDTO.getPassword());
        existingUser.setRoles(userDTO.getRoles());
        existingUser.setUsername(userDTO.getUsername());

        userRepository.saveAndFlush(existingUser);
    }

    @Override
    public void deleteUser(Long id) throws NoMatchException {
        User existingUser = getExistingUserById(id);
        userRepository.delete(existingUser);
    }

    @Override
    public void updateUserVacations(Vacation vacation) throws NoMatchException {
        User existingUser = getExistingUserById(vacation.getUserId());
        existingUser.getVacations().add(vacation);
        userRepository.saveAndFlush(existingUser);
    }

    @Override
    public User getUserByVacationId(Long vacationId) throws NoMatchException {
        Optional<User> existingUser = userRepository.findByVacationId(vacationId);

        return getUser(existingUser);
    }

    private User getExistingUserById(Long id) throws NoMatchException {
        Optional<User> existingUser = userRepository.findById(id);

        return getUser(existingUser);
    }

    private User getUser(Optional<User> existingUser) throws NoMatchException {
        if (!existingUser.isPresent()) {
            throw new NoMatchException("The id provided doesn't match any user");
        }

        return existingUser.get();
    }
}
