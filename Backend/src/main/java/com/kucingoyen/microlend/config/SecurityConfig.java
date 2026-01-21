package com.kucingoyen.microlend.config;

import com.kucingoyen.microlend.service.GoogleUserService;
import com.kucingoyen.microlend.util.JwtAuthenticationFilter;
import com.kucingoyen.microlend.util.OAuth2SuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;
    private final GoogleUserService googleUserService;
    private final OAuth2SuccessHandler successHandler;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter,
                          GoogleUserService googleUserService,
                          OAuth2SuccessHandler successHandler) {
        this.jwtFilter = jwtFilter;
        this.googleUserService = googleUserService;
        this.successHandler = successHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/oauth2/**", "/login/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo ->
                                userInfo.userService(googleUserService)
                        )
                        .successHandler(successHandler)
                )
                .addFilterBefore(jwtFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

