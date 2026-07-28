package com.dealership.api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Validates the JWT in the Authorization header and populates the
 * SecurityContext so @PreAuthorize("hasRole('ADMIN')") works.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);

    private final JwtTokenProvider tokenProvider;

    // Constructor for dependency injection
    public JwtAuthFilter(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest  request,
        HttpServletResponse response,
        FilterChain         filterChain
    ) throws ServletException, IOException {

        String token = resolveToken(request);
        String method = request.getMethod();
        String uri = request.getRequestURI();

        if (StringUtils.hasText(token)) {
            if (tokenProvider.validateToken(token)) {
                String username = tokenProvider.getUsername(token);
                String role = tokenProvider.getRole(token);
                var auth = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_" + role))
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                log.warn("JWT INVALID for {} {} — token starts: {}…", method, uri,
                    token.substring(0, Math.min(token.length(), 20)));
            }
        } else if (!"GET".equals(method) && !uri.contains("/login") && !uri.contains("/public")) {
            log.warn("NO TOKEN for {} {} — Auth header: {}", method, uri,
                request.getHeader("Authorization"));
        }

        filterChain.doFilter(request, response);
    }

    /** Extract bearer token from Authorization header, falling back to ?token= query param. */
    private String resolveToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        // Fallback: accept token as query parameter (browsers sometimes strip auth headers)
        String queryToken = request.getParameter("token");
        if (StringUtils.hasText(queryToken)) {
            return queryToken;
        }
        return null;
    }
}
