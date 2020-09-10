package com.consultant.model.security;

import com.consultant.model.entities.User;
import com.consultant.model.repositories.UserRepository;
import com.consultant.model.services.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.session.SessionManagementFilter;

import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAuthenticationSuccessHandler authenticationSuccessHandler;

    @Bean
    CorsFilter corsFilter() {
        return new CorsFilter();
    }

    @Autowired
    private UserService userService;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .addFilterBefore(corsFilter(), SessionManagementFilter.class)
                .headers().frameOptions().disable()
                .and()
                .csrf().disable()
                .authorizeRequests().antMatchers("/h2-console/**").permitAll()

//                .authorizeRequests().antMatchers(HttpMethod.GET, "/user/**").authenticated()
//                .authorizeRequests().antMatchers("/user/**").hasAuthority("admin")

                // all other requests need to be authenticated
                .anyRequest().authenticated()
                .and()
                .oauth2Login()

//                .userInfoEndpoint(userInfo -> userInfo
//                        .userService(this.oidcUserService()))

                // will be invoked after successful login
                .successHandler(authenticationSuccessHandler)
                .and()

                // if you want a 401 instead of redirect to google login (in example frontend then called google login)
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))

                // logout
                .logout().invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutSuccessUrl("/login?logout")
                .deleteCookies("JSESSIONID").permitAll();
    }

//    @Bean
//    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oidcUserService() {
//        final OAuth2UserService delegate = new DefaultOAuth2UserService();
//
//        return (userRequest) -> {
//            // Delegate to the default implementation for loading a user
//            OAuth2User oidcUser = delegate.loadUser(userRequest);
//
//            String email = oidcUser.getAttribute("email");
//
//            User existingUser = userService.getUserByEmail(email);
//
//            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
//            existingUser.getRoles().forEach(role -> {
//                mappedAuthorities.add(new SimpleGrantedAuthority(role));
//                System.out.println(role);
//            });
//
//            oidcUser = new DefaultOAuth2User(mappedAuthorities, oidcUser.getAttributes(), "name");
//
//            return oidcUser;
//        };
//    }

}