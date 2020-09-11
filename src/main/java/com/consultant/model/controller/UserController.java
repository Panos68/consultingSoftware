package com.consultant.model.controller;

import com.consultant.model.dto.UserDTO;
import com.consultant.model.entities.User;
import com.consultant.model.exception.EmailMissingException;
import com.consultant.model.exception.ForbiddenException;
import com.consultant.model.exception.UnsupportedAuthenticationMethodException;
import com.consultant.model.mappers.UserMapper;
import com.consultant.model.services.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@CrossOrigin(allowCredentials = "true")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Set<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping(value = "/info")
    public ResponseEntity<UserDTO> getUserInfo() {
        String email = getCurrentUserEmail();
        User user = userService.getUserByEmail(email);
        UserDTO userDTO = UserMapper.INSTANCE.userToUserDTO(user);

        return ResponseEntity.ok(userDTO);
    }

    @PutMapping
    public void editUser(@RequestBody UserDTO userDTO) throws Exception {
        validateUserHasAdminRole();

        userService.edit(userDTO);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteUser(@PathVariable Long id) throws Exception {
        validateUserHasAdminRole();
        userService.delete(id);
    }

    @GetMapping(value = "/logout")
    public void logoutUser() throws Exception {
        System.out.println("logout");
    }

    private void validateUserHasAdminRole() {
        String email = getCurrentUserEmail();

        User user = userService.getUserByEmail(email);

        if (user.getRoles().stream().noneMatch(role -> role.equals("admin"))) {
            throw new ForbiddenException();
        }
    }

    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;

            try {
                return oauth2Token.getPrincipal().getAttribute("email");
            } catch (Exception e) {
                throw new EmailMissingException("\"No email provided from the Oauth2 authentication service provider\"");
            }
        } else {
            throw new UnsupportedAuthenticationMethodException("Authentication method does not match with the allowed OAuth2 providers");
        }
    }
}