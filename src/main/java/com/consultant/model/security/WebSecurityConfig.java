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
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.session.SessionManagementFilter;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private CustomLogoutSuccessHandler logoutSuccessHandler;

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

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
                .authorizationEndpoint()
                .authorizationRequestResolver(new CustomAuthorizationRequestResolver(this.clientRegistrationRepository))
                .and()

                // will be invoked after successful login
                .successHandler(authenticationSuccessHandler)
                .and()

                // if you want a 401 instead of redirect to google login
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))

                // logout
                .logout().invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutSuccessHandler(logoutSuccessHandler)
                .deleteCookies("JSESSIONID").permitAll();
    }


    public class CustomAuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {
        private final OAuth2AuthorizationRequestResolver defaultAuthorizationRequestResolver;

        public CustomAuthorizationRequestResolver(
                ClientRegistrationRepository clientRegistrationRepository) {

            this.defaultAuthorizationRequestResolver =
                    new DefaultOAuth2AuthorizationRequestResolver(
                            clientRegistrationRepository, "/oauth2/authorization");
        }

        @Override
        public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
            OAuth2AuthorizationRequest authorizationRequest =
                    this.defaultAuthorizationRequestResolver.resolve(request);

            return authorizationRequest != null ?
                    customAuthorizationRequest(authorizationRequest) :
                    null;
        }

        @Override
        public OAuth2AuthorizationRequest resolve(
                HttpServletRequest request, String clientRegistrationId) {

            OAuth2AuthorizationRequest authorizationRequest =
                    this.defaultAuthorizationRequestResolver.resolve(
                            request, clientRegistrationId);

            return authorizationRequest != null ?
                    customAuthorizationRequest(authorizationRequest) :
                    null;
        }

        private OAuth2AuthorizationRequest customAuthorizationRequest(
                OAuth2AuthorizationRequest authorizationRequest) {

            Map<String, Object> additionalParameters =
                    new LinkedHashMap<>(authorizationRequest.getAdditionalParameters());
            additionalParameters.put("prompt", "select_account");

            return OAuth2AuthorizationRequest.from(authorizationRequest)
                    .additionalParameters(additionalParameters)
                    .build();
        }
    }

}