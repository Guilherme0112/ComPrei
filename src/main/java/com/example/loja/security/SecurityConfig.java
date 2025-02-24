package com.example.loja.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/login", "/auth/register").anonymous()
                .requestMatchers("/", "/css/**", "/js/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(formLogin -> formLogin.disable())
            .httpBasic(httpBasic -> httpBasic.disable())
            .logout(logout -> logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/auth/login")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
            )
            .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                    .maximumSessions(1)
                    .expiredUrl("/auth/login")
            );

            return http.build();
            
    }
}
