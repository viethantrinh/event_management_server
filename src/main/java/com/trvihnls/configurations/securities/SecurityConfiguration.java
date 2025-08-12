package com.trvihnls.configurations.securities;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.awt.*;
import java.util.List;
import java.util.Set;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final CustomJWTDecoder customJWTDecoder;
    private final JwtAuthenticationEntryPointHandler authenticationHandler;
    private final JwtAccessDeniedHandler authorizationHandler;

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
                        .anyRequest().authenticated())
                .oauth2ResourceServer((o2rc) -> o2rc
                        .jwt((jwtc) -> jwtc
                                .decoder(customJWTDecoder)
                                .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                        .authenticationEntryPoint(authenticationHandler)
                        .accessDeniedHandler(authorizationHandler)
                )
                .exceptionHandling(eh -> eh
                        .authenticationEntryPoint(authenticationHandler)
                        .accessDeniedHandler(authorizationHandler));

        return httpSecurity.build();
    }

    /**
     * Configures a JWT authentication converter for Spring Security.
     * This method customizes how authorities (roles) are extracted from JWT tokens
     * by adding a "ROLE_" prefix to each authority.
     *
     * @return A configured {@link JwtAuthenticationConverter} that extracts and prefixes authorities.
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        // Create a converter to extract authorities from JWT claims
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        // Set the prefix for authorities to "ROLE_" (e.g., "admin" becomes "ROLE_admin")
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        // Create the main JWT authentication converter
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();

        // Configure it to use the custom authorities converter
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }

    @Bean
    public PasswordEncoder configurePasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
