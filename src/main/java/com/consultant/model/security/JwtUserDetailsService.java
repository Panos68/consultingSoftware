package com.consultant.model.security;

import com.consultant.model.dto.UserDTO;
import com.consultant.model.entities.User;
import com.consultant.model.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

//@Service
public class JwtUserDetailsService  {
//    private UserRepository userRepository;
//
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    public JwtUserDetailsService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = getAuthenticationUser(username);
//        return new org.springframework.security.core.userdetails.User(user.getUsername(), passwordEncoder.encode(user.getPassword()), user.getAuthorities());
//    }
//
//    public Collection<? extends GrantedAuthority> authenticateUserAndReturnAuthorities(UserDTO userDTO) throws WrongValidationException {
//        User authenticationUser = getAuthenticationUser(userDTO.getUsername());
//        if (!passwordEncoder.matches(userDTO.getPassword(),authenticationUser.getPassword())){
//            throw new WrongValidationException("Incorrect credentials");
//        }
//        return authenticationUser.getAuthorities();
//    }
//
//    public User getAuthenticationUser(String  userName) {
//        Optional<User> authenticationUser = userRepository.findByUsername(userName);
//        if (!authenticationUser.isPresent()){
//            throw new UsernameNotFoundException("No existing user with that name");
//        }
//        return authenticationUser.get();
//    }
}