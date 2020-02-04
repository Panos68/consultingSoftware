package com.consultant.model.services;

import com.consultant.model.dto.UserDTO;
import com.consultant.model.exception.NoMatchException;

import java.util.Set;

public interface UserService {
    Set<UserDTO> getAllUsers();

    void createUser(UserDTO userDTO);

    void editUser(UserDTO userDTO) throws NoMatchException;

    void deleteUser(Long id) throws NoMatchException;
}
