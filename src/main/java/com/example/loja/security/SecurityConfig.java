package com.example.loja.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/auth/login", "/auth/register").anonymous()
                .requestMatchers("/", "/css/**", "/js/**", "/email/**", "/icons/**", "/reset/**", "/uploads/**", "/produto/**", "/carrinho/**", "/payment/**").permitAll()
                .anyRequest().authenticated()
            )

            .exceptionHandling(exception -> exception
                .accessDeniedHandler(accessDeniedHandler())
                .authenticationEntryPoint((request, response, authException) -> {
                    response.sendRedirect("/auth/login");
                })
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
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .maximumSessions(1)
                .expiredUrl("/")
            );

            return http.build();

    }
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.sendRedirect("/");
        };
    }
}
