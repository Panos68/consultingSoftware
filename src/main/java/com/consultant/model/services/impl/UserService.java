package com.consultant.model.services.impl;

import com.consultant.model.dto.UserDTO;
import com.consultant.model.entities.Consultant;
import com.consultant.model.entities.User;
import com.consultant.model.entities.Vacation;
import com.consultant.model.exception.EntityAlreadyExists;
import com.consultant.model.exception.NoMatchException;
import com.consultant.model.exception.NotFoundException;
import com.consultant.model.mappers.UserMapper;
import com.consultant.model.repositories.UserRepository;
import com.consultant.model.services.BasicOperationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService implements BasicOperationsService<UserDTO> {

    private UserRepository userRepository;

    private ConsultantService consultantService;


    @Autowired
    public UserService(UserRepository userRepository, ConsultantService consultantService) {
        this.userRepository = userRepository;
        this.consultantService = consultantService;
    }


    @Override
    public Set<UserDTO> getAll() {
        List<User> usersList = userRepository.findAll();
        Set<UserDTO> userDTOS = new LinkedHashSet<>();
        usersList.forEach(user -> {
            final UserDTO userDTO = UserMapper.INSTANCE.userToUserDTO(user);
            Optional<Consultant> consultantOfUser = consultantService.getConsultantOfUser(userDTO.getId());
            consultantOfUser.ifPresent(consultant -> userDTO.setConsultantId(consultant.getId()));
            userDTOS.add(userDTO);
        });

        return userDTOS;
    }

    // TODO fix this
    @Override
    public Long create(UserDTO userDTO) throws EntityAlreadyExists {
        return null;
    }

    @Override
    public void edit(UserDTO userDTO) throws NoMatchException {
        User existingUser = getExistingUserById(userDTO.getId());

        updateUser(existingUser, userDTO);
    }

    @Override
    public void delete(Long userId) throws NoMatchException {
        User existingUser = getExistingUserById(userId);

        consultantService.unAssignUserFromConsultant(userId);

        userRepository.delete(existingUser);
    }

    public void updateUserVacations(Vacation vacation) throws NoMatchException {
        User existingUser = getExistingUserById(vacation.getUserId());
        existingUser.getVacations().add(vacation);
        userRepository.saveAndFlush(existingUser);
    }

    public User getUserByVacationId(Long vacationId) throws NoMatchException {
        return userRepository.findByVacationId(vacationId)
                .orElseThrow(() -> new NoMatchException("The id provided doesn't match any user"));
    }

    private void updateUser(User existingUser, UserDTO userDTO) {
        existingUser.setRoles(userDTO.getRoles());

        userRepository.saveAndFlush(existingUser);
    }

    private User getExistingUserById(Long id) throws NoMatchException {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoMatchException("The id provided doesn't match any user"));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(NotFoundException::new);
    }
}
