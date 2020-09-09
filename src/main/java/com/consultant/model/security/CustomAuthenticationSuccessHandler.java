package com.consultant.model.security;

import com.consultant.model.entities.Oauth2User;
import com.consultant.model.entities.Oauth2UserInfo;
import com.consultant.model.repositories.OauthUserRepository;
import com.consultant.model.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
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
    private OauthUserRepository oAuth2UserRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("Loggat in!");

        if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User principal = (OAuth2User) authentication.getPrincipal();
            processOAuth2User(principal);
        }

        int i = 0;
    }

    private void processOAuth2User(OAuth2User oAuth2User) {
        Oauth2UserInfo oauth2UserInfo = new Oauth2UserInfo(oAuth2User.getAttributes());

        if (oauth2UserInfo.getEmail() == null) {
            // TODO throw exception if email is not provided
        }

        Optional<Oauth2User> existingUser = oAuth2UserRepository.findByEmail(oauth2UserInfo.getEmail());

        if (!existingUser.isPresent()) {
            registerNewUser(oauth2UserInfo);
        }

        // TODO if present - update info? which info?
    }

    private void registerNewUser(Oauth2UserInfo oauth2UserInfo) {

        ArrayList<String> roles = new ArrayList<>();
        roles.add("user");

        Oauth2User oauth2User = Oauth2User.builder()
                .email(oauth2UserInfo.getEmail())
                .roles(roles)
                .build();

        oAuth2UserRepository.save(oauth2User);

        Optional<Oauth2User> byEmail = oAuth2UserRepository.findByEmail(oauth2User.getEmail());

        int i = 2;
    }
}
