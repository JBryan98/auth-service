package com.jbryan98.authservice.authentication.infrastructure.security;

import com.jbryan98.authservice.shared.GlobalExceptionHandler;
import io.jsonwebtoken.JwtException;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final HandlerExceptionResolver handlerExceptionResolver;

    /**
     * This filter is executed once per request.
     * It validates the JWT token and sets the authentication
     * in the SecurityContext if the token is valid.
     */
    @Override
    protected void doFilterInternal(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull FilterChain filterChain
    ) throws ServletException, IOException {
        // Skip JWT validation for authentication endpoints
        if (request.getServletPath().startsWith("/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }


        final String authHeader = request.getHeader(AUTHORIZATION);
        final String jwt;
        final String username;

        // If there is no Authorization header, or it does not start with "Bearer ",
        // continue the filter chain without authentication
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract the JWT token from the header
        jwt = authHeader.substring(7);
        try {
            username = jwtService.extractUsername(jwt);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Load the user from the database
                var userDetails = userDetailsService.loadUserByUsername(username);
                if (!userDetails.isEnabled()) {
                    throw new DisabledException("User account is disabled.");
                }
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    // Attach request details (IP, session, etc.)
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // Set authentication in the SecurityContext
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (DisabledException | UsernameNotFoundException | JwtException ex) {
            /**
             * Delegates the exception handling to the configured HandlerExceptionResolver
             * Errors are now handled by {@link GlobalExceptionHandler}, which returns a RFC 9457 problem detail
             */
            handlerExceptionResolver.resolveException(request, response, null, ex);
            return; // Avoid continuing the filter chain after the exception has been handled
        }
        filterChain.doFilter(request, response);
    }
}

