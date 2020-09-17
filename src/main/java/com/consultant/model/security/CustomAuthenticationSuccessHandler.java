package com.consultant.model.security;

import com.consultant.model.config.AppProperties;
import com.consultant.model.dto.Oauth2UserDTO;
import com.consultant.model.entities.User;
import com.consultant.model.exception.EmailMissingException;
import com.consultant.model.exception.UnsupportedAuthenticationMethodException;
import com.consultant.model.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User principal = (OAuth2User) authentication.getPrincipal();
            processOAuth2User(principal);
        } else {
            throw new UnsupportedAuthenticationMethodException("Only supported method for authentication is Oauth2 with Google");
        }

        String targetUrl = getTargetUrl();
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private String getTargetUrl() {
        return appProperties.getOauth2().getAuthorizedRedirectUri();
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

    private void registerNewUser(Oauth2UserDTO oauth2UserDTO) {
        User user = User.builder()
                .email(oauth2UserDTO.getEmail())
                .roles(List.of("user"))
                .build();

        userRepository.save(user);
    }
}
