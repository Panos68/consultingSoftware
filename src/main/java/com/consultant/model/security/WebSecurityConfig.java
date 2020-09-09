package com.consultant.model.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

//    private JwtRequestFilter jwtRequestFilter;

//    @Autowired
//    private CustomOauthService oauthService;

    @Autowired
    private CustomAuthenticationSuccessHandler successHandler;

//    @Bean
//    CorsFilter corsFilter() {
//        return new CorsFilter();
//    }

//    @Autowired
//    public WebSecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtRequestFilter jwtRequestFilter) {
//        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
//        this.jwtRequestFilter = jwtRequestFilter;
//    }

//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .headers().frameOptions().disable()
                .and()
                .csrf().disable()
                .authorizeRequests().antMatchers("/h2-console/**").permitAll().and()

                .authorizeRequests().antMatchers(HttpMethod.GET, "/user/**").authenticated().and()
                .authorizeRequests().antMatchers("/user/**").hasAuthority("admin")

                // all other requests need to be authenticated
                .anyRequest().authenticated()
                .and()
                .oauth2Login()

                // will be invoked after successful login
                .successHandler(successHandler)
                .and()

                // if you want a 401 instead of redirect to google login (in example frontend then called google login)
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))

                // logout
                .logout(l -> l
                        .logoutSuccessUrl("/").permitAll()
                );

    }

//    @Override
//    protected void configure(HttpSecurity httpSecurity) throws Exception {
//        // We don't need CSRF for this example
//        httpSecurity
////                .addFilterBefore(corsFilter(), SessionManagementFilter.class)
//                .csrf().disable()
//
//
//                .oauth2Login().and()
//
//                // dont authenticate this particular request
//                .authorizeRequests().antMatchers("/user/authenticate").permitAll().and()
//                .authorizeRequests().antMatchers(HttpMethod.GET,"/user/**").authenticated().and().
//                authorizeRequests().antMatchers("/user/**").hasAuthority("admin").
//                // all other requests need to be authenticated
//                        anyRequest().authenticated().and()
//
//                // make sure we use stateless session; session won't be used to
//                // store user's state.
//
////                        exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
//
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//        // Add a filter to validate the tokens with every request
////        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
//    }
}