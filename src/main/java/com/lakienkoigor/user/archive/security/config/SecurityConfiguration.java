package com.lakienkoigor.user.archive.security.config;

import com.lakienkoigor.user.archive.ui.view.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * Configuration entry point for vaadin and springframework security.
 */
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration extends VaadinWebSecurity {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    /**
     * Security configuration for authentication.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        setLoginView(http, LoginView.class);
        super.configure(http);
    }
}
