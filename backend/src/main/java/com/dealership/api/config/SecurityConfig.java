package com.dealership.api.config;

import com.dealership.api.security.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Spring Security configuration.
 * - Stateless JWT auth
 * - CORS configured from application.properties
 * - Public routes: GET /api/inventory/**, POST /api/contact, POST /api/admin/login
 * - Everything under /api/admin/** (except /login) requires ADMIN role
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Value("${dealership.cors.allowed-origins}")
    private String allowedOrigins;

    // Constructor for dependency injection
    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Public read access to inventory
                .requestMatchers(HttpMethod.GET,  "/api/inventory/**").permitAll()
                // Public feeds (Facebook Marketplace, etc.)
                .requestMatchers(HttpMethod.GET,  "/api/feeds/**").permitAll()
                // Public contact form submission
                .requestMatchers(HttpMethod.POST, "/api/contact").permitAll()
                // Public site settings (hours, name, etc.)
                .requestMatchers(HttpMethod.GET,  "/api/public/settings").permitAll()
                // Public staff directory
                .requestMatchers(HttpMethod.GET,  "/api/public/staff").permitAll()
                // Public reviews
                .requestMatchers(HttpMethod.GET,  "/api/public/reviews").permitAll()
                // Public financing application form
                .requestMatchers(HttpMethod.POST, "/api/financing-application").permitAll()
                // Public service appointment request
                .requestMatchers(HttpMethod.POST, "/api/schedule-service").permitAll()
                // Public email test endpoint (for OpenClaw)
                .requestMatchers(HttpMethod.POST, "/api/email/test").permitAll()
                // Admin dashboard stats
                .requestMatchers(HttpMethod.GET, "/api/admin/dashboard").authenticated()
                // Admin login — public
                .requestMatchers(HttpMethod.POST, "/api/admin/login").permitAll()
                // Manager login — public
                .requestMatchers(HttpMethod.POST, "/api/manager/login").permitAll()
                // Everything else requires authentication
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(Arrays.asList(allowedOrigins.split(",")));
        config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS","PATCH"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
