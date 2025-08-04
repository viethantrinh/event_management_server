package com.trvihnls.configurations.securities;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.awt.*;
import java.util.List;
import java.util.Set;

@Configuration
public class SecurityConfiguration {

    private final Set<String> whiteList = Set.of(
            "/auth/**"
    );

    @Bean
    public SecurityFilterChain configureSecurity(HttpSecurity httpSecurity)
            throws Exception {

        httpSecurity
                .cors(Customizer.withDefaults())
                .csrf((csrf) -> csrf.disable())
                .sessionManagement((sm) ->
                        sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((authorizeHttpRequest) -> authorizeHttpRequest
                        .requestMatchers(whiteList.toArray(new String[]{})).permitAll()
                        .anyRequest().authenticated()
                )
        ;

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder configurePasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
