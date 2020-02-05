package com.consultant.model.security;

import com.consultant.model.dto.UserDTO;
import com.consultant.model.entities.User;
import com.consultant.model.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class JwtUserDetailsServiceShould {

    private JwtUserDetailsService jwtUserDetailsService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private String correctUsername = "correctUsername";


    private String correctPassword = "correctPassword";

    private SimpleGrantedAuthority adminRole = new SimpleGrantedAuthority("admin");

    private User user = new User();

    private UserDTO userDTO = new UserDTO();

    private User savedUser = new User();

    @Before
    public void setUp() {
        jwtUserDetailsService = new JwtUserDetailsService(userRepository,passwordEncoder);
        savedUser.setUsername(correctUsername);
        savedUser.setPassword(correctPassword);
        Mockito.when(passwordEncoder.encode(Mockito.any())).thenReturn("encodedPassword");
    }

    @Test
    public void loadUserOnExistingUsername() {
        user.setUsername(correctUsername);
        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(java.util.Optional.ofNullable(savedUser));
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(user.getUsername());
        Assert.assertEquals(userDetails.getUsername(), correctUsername);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void throwErrorOnNonExistingUsername() {
        String incorrectUsername = "incorrectUsername";
        user.setUsername(incorrectUsername);
        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        jwtUserDetailsService.loadUserByUsername(user.getUsername());
    }

    @Test
    public void returnAuthoritiesOnCorrectAuthentication() throws Exception {
        userDTO.setPassword(correctPassword);
        userDTO.setUsername(correctUsername);
        savedUser.getRoles().add(adminRole.toString());

        Mockito.when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(java.util.Optional.ofNullable(savedUser));
        Collection<? extends GrantedAuthority> grantedAuthorities = jwtUserDetailsService.authenticateUserAndReturnAuthorities(userDTO);
        Assert.assertTrue(grantedAuthorities.contains(adminRole));
    }

    @Test(expected = WrongValidationException.class)
    public void throwErrorOnIncorrectAuthentication() throws Exception {
        String inCorrectPassword = "incorrectPassword";
        user.setPassword(inCorrectPassword);
        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(java.util.Optional.ofNullable(savedUser));
        jwtUserDetailsService.authenticateUserAndReturnAuthorities(userDTO);
    }
}