package com.consultant.model.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    AuthenticationUserRepository authenticationUserRepository;

    PasswordEncoder passwordEncoder;

    @Autowired
    public JwtUserDetailsService(AuthenticationUserRepository authenticationUserRepository,PasswordEncoder passwordEncoder) {
        this.authenticationUserRepository = authenticationUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthenticationUser authenticationUser = getAuthenticationUser(username);
        return new User(authenticationUser.getUsername(), passwordEncoder.encode(authenticationUser.getPassword()),authenticationUser.getAuthorities());
    }

    public void authenticateUser(AuthenticationUser user) throws Exception {
        AuthenticationUser authenticationUser = getAuthenticationUser(user.getUsername());
        if (!authenticationUser.getPassword().equals(user.getPassword())){
            throw new Exception("Incorrect credentials");
        }
    }

    private AuthenticationUser getAuthenticationUser(String  userName) {
        Optional<AuthenticationUser> authenticationUser = authenticationUserRepository.findByUsername(userName);
        if (!authenticationUser.isPresent()){
            throw new UsernameNotFoundException("No existing user with that name");
        }
        return authenticationUser.get();
    }
}