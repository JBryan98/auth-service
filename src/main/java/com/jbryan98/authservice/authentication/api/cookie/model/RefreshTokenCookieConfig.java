package com.jbryan98.authservice.authentication.api.cookie.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "application.security.refresh-token")
@Validated
public record RefreshTokenCookieConfig(
        @NotNull(message = "Refresh token cookie expirationDays is required")
        @Positive(message = "Refresh token cookie expirationDays must be a positive number")
        long expirationDays,

        @NotNull(message = "Refresh token cookieSecure flag is required")
        boolean cookieSecure,

        @NotNull(message = "Refresh token cookieSameSite flag is required")
        String cookieSameSite,

        @NotNull(message = "Refresh token cookiePath is required")
        String cookiePath,

        @NotNull(message = "Refresh token cookieDomain domain is required")
        String cookieDomain
) {
}
