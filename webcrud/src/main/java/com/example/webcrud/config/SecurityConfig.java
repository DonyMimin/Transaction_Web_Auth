package com.example.webcrud.config;

import com.example.webcrud.security.JwtFilter;
import com.example.webcrud.security.JwtTokenProvider;

import jakarta.servlet.http.Cookie;

import com.example.webcrud.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // Initialize variable
    private final CustomUserDetailsService userDetailsService;
    private final JwtFilter jwtFilter;
    private final JwtTokenProvider jwtService;

    // Add constructor
    public SecurityConfig(
        CustomUserDetailsService userDetailsService, 
        JwtFilter jwtFilter, 
        JwtTokenProvider jwtService
    ) {
        this.userDetailsService = userDetailsService;
        this.jwtFilter = jwtFilter;
        this.jwtService = jwtService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    new AntPathRequestMatcher("/login"),
                    new AntPathRequestMatcher("/auth/login"),
                    new AntPathRequestMatcher("/register"),
                    new AntPathRequestMatcher("/register/**"),
                    new AntPathRequestMatcher("/")
                ).permitAll()
                .requestMatchers(
                    new AntPathRequestMatcher("/home"),
                    new AntPathRequestMatcher("/home/**")
                ).authenticated()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler((request, response, authentication) -> {
                    // Manually add JWT token creation logic here
                    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                    String token = jwtService.generateToken(userDetails);
                    
                    // Create JWT cookie
                    Cookie cookie = new Cookie("jwt_token", token);
                    cookie.setHttpOnly(true);
                    cookie.setPath("/");
                    cookie.setMaxAge(24 * 60 * 60); // 1 day
                    response.addCookie(cookie);
                    
                    response.sendRedirect("/home");
                })
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .deleteCookies("jwt_token")
                .invalidateHttpSession(true)
            );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}