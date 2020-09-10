package com.consultant.model.controller;

import com.consultant.model.dto.UserDTO;
import com.consultant.model.entities.User;
import com.consultant.model.exception.EmailMissingException;
import com.consultant.model.exception.ForbiddenException;
import com.consultant.model.mappers.UserMapper;
import com.consultant.model.services.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Set<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping(value = "/info")
    public ResponseEntity<UserDTO> getUserInfo(OAuth2AuthenticationToken authentication) {
        String email;
        try {
            email = authentication.getPrincipal().getAttribute("email");
        } catch (Exception e) {
            throw new EmailMissingException("\"No email provided from the Oauth2 authentication service provider\"");
        }
        User user = userService.getUserByEmail(email);
        UserDTO userDTO = UserMapper.INSTANCE.userToUserDTO(user);

        return ResponseEntity.ok(userDTO);
    }

    @PutMapping
    public void editUser(@RequestBody UserDTO userDTO, OAuth2AuthenticationToken authentication) throws Exception {
        validateUserHasAdminRole(authentication);
        userService.edit(userDTO);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteUser(@PathVariable Long id, OAuth2AuthenticationToken authentication) throws Exception {
        validateUserHasAdminRole(authentication);
        userService.delete(id);
    }

    private void validateUserHasAdminRole(OAuth2AuthenticationToken authentication){
        String email;
        try {
            email = authentication.getPrincipal().getAttribute("email");
        } catch (Exception e) {
            throw new EmailMissingException("\"No email provided from the Oauth2 authentication service provider\"");
        }
        User user = userService.getUserByEmail(email);

        if(user.getRoles().stream().noneMatch(role -> role.equals("admin"))){
            throw new ForbiddenException();
        }
    }
}