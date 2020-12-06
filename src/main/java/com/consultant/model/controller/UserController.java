package com.consultant.model.controller;

import com.consultant.model.dto.UserDTO;
import com.consultant.model.entities.User;
import com.consultant.model.security.JwtResponse;
import com.consultant.model.security.JwtTokenUtil;
import com.consultant.model.security.JwtUserDetailsService;
import com.consultant.model.security.WrongValidationException;
import com.consultant.model.services.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    private JwtTokenUtil jwtTokenUtil;
    private JwtUserDetailsService userDetailsService;
    private UserService userService;

    @Autowired
    public UserController(JwtTokenUtil jwtTokenUtil, JwtUserDetailsService userDetailsService, UserService userService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody UserDTO userDTO) throws WrongValidationException {
        Collection<? extends GrantedAuthority> grantedAuthorities = userDetailsService.authenticateUserAndReturnAuthorities(userDTO);
        final String token = jwtTokenUtil.generateToken(userDTO.getUsername());
        User authenticationUser = userDetailsService.getAuthenticationUser(userDTO.getUsername());
        return ResponseEntity.ok(new JwtResponse(token, grantedAuthorities, userDTO.getUsername(), authenticationUser.getId()));
    }

    @GetMapping
    public ResponseEntity<Set<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAll());
    }

    @PostMapping
    public Long createUser(@RequestBody UserDTO userDTO) throws Exception {
        return userService.create(userDTO);
    }

    @PutMapping
    public void editUser(@RequestBody UserDTO userDTO) throws Exception {
        userService.edit(userDTO);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteUser(@PathVariable Long id) throws Exception {
        userService.delete(id);
    }

    @PatchMapping(value = "{id}/password")
    public ResponseEntity<String> updatePassword(@PathVariable Long id, @RequestParam String password) throws Exception {
        userService.updatePassword(id, password);

        return new ResponseEntity<>("Password successfully updated", HttpStatus.OK);
    }
}