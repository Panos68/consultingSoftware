package com.consultant.model.security;

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
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Optional;



@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";

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

//        String targetUrl = determineTargetUrl(request, response, authentication);
        String targetUrl = "http://localhost:3000/oauth2/redirect";
//        if (response.isCommitted()) {
//            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
//            return;
//        }

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }


//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        String targetUrl = determineTargetUrl(request, response, authentication);
//
//        if (response.isCommitted()) {
//            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
//            return;
//        }
//
//        clearAuthenticationAttributes(request, response);
//        getRedirectStrategy().sendRedirect(request, response, targetUrl);
//    }

    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return Optional.of(cookie);
                }
            }
        }

        return Optional.empty();
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

//        if(redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
//            throw new BadRequestException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
//        }

        return redirectUri.orElse(getDefaultTargetUrl());
    }

//    private boolean isAuthorizedRedirectUri(String uri) {
//        URI clientRedirectUri = URI.create(uri);
//
//        return appProperties.getOauth2().getAuthorizedRedirectUris()
//                .stream()
//                .anyMatch(authorizedRedirectUri -> {
//                    // Only validate host and port. Let the clients use different paths if they want to
//                    URI authorizedURI = URI.create(authorizedRedirectUri);
//                    if(authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
//                            && authorizedURI.getPort() == clientRedirectUri.getPort()) {
//                        return true;
//                    }
//                    return false;
//                });
//    }


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
