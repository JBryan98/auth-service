package com.jbryan98.authservice.users.infrastructure.persistence.config;

import com.jbryan98.authservice.users.domain.entity.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class ApplicationAuditAware implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        // Scheduled jobs, batch processes, async tasks
        if (authentication == null) {
            return Optional.of("SYSTEM");
        }
        // Unauthenticated user
        if (!authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            return Optional.of("ANONYMOUS");
        }
        var principal = (User) authentication.getPrincipal();
        return Optional.ofNullable(principal.getUsername());
    }
}