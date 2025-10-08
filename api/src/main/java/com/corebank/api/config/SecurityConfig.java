package com.corebank.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for Postman testing
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/users/register").permitAll() // public endpoint
                        .anyRequest().authenticated()                   // others require auth
                )
                .httpBasic(httpBasic -> {}); // Enable HTTP Basic Auth for other endpoints

        return http.build();
    }
}
