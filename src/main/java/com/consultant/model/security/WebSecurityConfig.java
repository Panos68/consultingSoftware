package com.consultant.model.security;

import com.consultant.model.services.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.session.SessionManagementFilter;

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
                .authorizeRequests()

                // can be used for testing
                // .antMatchers("/h2-console/**").permitAll()

                // all requests need to be authenticated
                .anyRequest().authenticated()
                .and()
                .oauth2Login()

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