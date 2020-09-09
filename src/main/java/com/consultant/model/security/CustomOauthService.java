package com.consultant.model.security;

import com.consultant.model.entities.Oauth2User;
import com.consultant.model.entities.Oauth2UserInfo;
import com.consultant.model.repositories.OauthUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//@Service
public class CustomOauthService extends DefaultOAuth2UserService {

    @Autowired
    private OauthUserRepository oAuth2UserRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

//        try {
        processOAuth2User(oAuth2User);
//        } catch (AuthenticationException ex) {
//            throw ex;
//        } catch (Exception ex) {
//            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
////            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
//        }

        return null;
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

//        oAuth2UserRepository.save(oauth2User);
    }

}
