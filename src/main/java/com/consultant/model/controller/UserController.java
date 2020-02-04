package com.consultant.model.controller;

import com.consultant.model.dto.UserDTO;
import com.consultant.model.entities.User;
import com.consultant.model.security.*;
import com.consultant.model.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<?> createAuthenticationToken(@RequestBody User user) throws WrongValidationException {
        Collection<? extends GrantedAuthority> grantedAuthorities = userDetailsService.authenticateUserAndReturnAuthorities(user);
        final String token = jwtTokenUtil.generateToken(user.getUsername());
        return ResponseEntity.ok(new JwtResponse(token, grantedAuthorities));
    }

    @GetMapping
    public ResponseEntity<Set<UserDTO>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping
    public void createUser(@RequestBody UserDTO userDTO) throws Exception {
        userService.createUser(userDTO);
    }

    @PutMapping
    public void editTeam(@RequestBody UserDTO userDTO) throws Exception {
        userService.editUser(userDTO);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteTeam(@PathVariable Long id) throws Exception {
        userService.deleteUser(id);
    }
}