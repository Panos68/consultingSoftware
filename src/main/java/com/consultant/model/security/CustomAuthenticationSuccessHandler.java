package com.consultant.model.security;

import com.consultant.model.dto.Oauth2UserDTO;
import com.consultant.model.entities.User;
import com.consultant.model.exception.EmailMissingException;
import com.consultant.model.exception.UnsupportedAuthenticationMethodException;
import com.consultant.model.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("Loggat in!");

        if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User principal = (OAuth2User) authentication.getPrincipal();
            processOAuth2User(principal);
        } else {
            throw new UnsupportedAuthenticationMethodException("Supported method for authentication is only Oauth2 using google");
        }
    }

    private void processOAuth2User(OAuth2User oAuth2User) {
        Oauth2UserDTO oauth2UserDTO = new Oauth2UserDTO(oAuth2User.getAttributes());

        if (oauth2UserDTO.getEmail() == null) {
            throw new EmailMissingException("No email provided from the Oauth2 authentication service provider");
        }

        Optional<User> existingUser = userRepository.findByEmail(oauth2UserDTO.getEmail());

        if (!existingUser.isPresent()) {
            registerNewUser(oauth2UserDTO);
        }
    }

    // TODO change language level to >Java 8
    private void registerNewUser(Oauth2UserDTO oauth2UserDTO) {

        ArrayList<String> roles = new ArrayList<>();
        roles.add("user");

        User user = User.builder()
                .email(oauth2UserDTO.getEmail())
                .roles(roles)
                .build();

        userRepository.save(user);
    }
}
