package com.consultant.model.security;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class JwtUserDetailsServiceShould {

    private JwtUserDetailsService jwtUserDetailsService;

    @Mock
    AuthenticationUserRepository authenticationUserRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    private String correctUsername = "correctUsername";


    private String correctPassword = "correctPassword";

    private AuthenticationUser authenticationUser = new AuthenticationUser();

    private AuthenticationUser savedUser = new AuthenticationUser();

    @Before
    public void setUp() {
        jwtUserDetailsService = new JwtUserDetailsService(authenticationUserRepository,passwordEncoder);
        savedUser.setUsername(correctUsername);
        savedUser.setPassword(correctPassword);
        Mockito.when(passwordEncoder.encode(Mockito.any())).thenReturn("encodedPassword");
    }

    @Test
    public void loadUserOnExistingUsername() {
        authenticationUser.setUsername(correctUsername);
        Mockito.when(authenticationUserRepository.findByUsername(authenticationUser.getUsername())).thenReturn(java.util.Optional.ofNullable(savedUser));
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(authenticationUser.getUsername());
        Assert.assertEquals(userDetails.getUsername(), correctUsername);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void throwErrorOnNonExistingUsername() {
        String incorrectUsername = "incorrectUsername";
        authenticationUser.setUsername(incorrectUsername);
        Mockito.when(authenticationUserRepository.findByUsername(authenticationUser.getUsername())).thenReturn(Optional.empty());
        jwtUserDetailsService.loadUserByUsername(authenticationUser.getUsername());
    }

    @Test
    public void notThrowErrorOnCorrectAuthentication() throws Exception {
        authenticationUser.setPassword(correctPassword);
        Mockito.when(authenticationUserRepository.findByUsername(authenticationUser.getUsername())).thenReturn(java.util.Optional.ofNullable(savedUser));
        jwtUserDetailsService.authenticateUser(authenticationUser);
    }

    @Test(expected = WrongValidationException.class)
    public void throwErrorOnIncorrectAuthentication() throws Exception {
        String inCorrectPassword = "incorrectPassword";
        authenticationUser.setPassword(inCorrectPassword);
        Mockito.when(authenticationUserRepository.findByUsername(authenticationUser.getUsername())).thenReturn(java.util.Optional.ofNullable(savedUser));
        jwtUserDetailsService.authenticateUser(authenticationUser);
    }
}