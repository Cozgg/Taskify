package com.ccq.configs;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http    
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ).exceptionHandling(ex -> ex.authenticationEntryPoint((request, response, authException)
                -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Lỗi 401: Chưa xác thực Token!")
                ))
                .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        new AntPathRequestMatcher("/swagger-ui/**"),
                        new AntPathRequestMatcher("/swagger-ui.html"),
                        new AntPathRequestMatcher("/v3/api-docs/**"),
                        new AntPathRequestMatcher("/v3/api-docs"),
                        new AntPathRequestMatcher("/swagger-resources/**"),
                        new AntPathRequestMatcher("/webjars/**"),
                        new AntPathRequestMatcher("/")
                ).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/login")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/register")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/refresh-token")).permitAll()
                .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
