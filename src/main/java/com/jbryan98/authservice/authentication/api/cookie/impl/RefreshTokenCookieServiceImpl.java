package com.jbryan98.authservice.authentication.api.cookie.impl;

import com.jbryan98.authservice.authentication.api.cookie.RefreshTokenCookieService;
import com.jbryan98.authservice.authentication.api.cookie.model.RefreshTokenCookieConfig;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.WebUtils;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenCookieServiceImpl implements RefreshTokenCookieService {
    private final RefreshTokenCookieConfig config;

    @Override
    public ResponseCookie create(String refreshToken) {
        return baseCookieBuilder(refreshToken, Duration.ofDays(config.expirationDays())).build();
    }

    @Override
    public ResponseCookie clear() {
        return baseCookieBuilder("", Duration.ZERO).build();
    }

    @Override
    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return Optional.empty();
        }
        var cookie = WebUtils.getCookie(request, COOKIE_NAME);
        if (cookie == null || cookie.getValue() == null || cookie.getValue().isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(cookie.getValue());
    }

    private ResponseCookie.ResponseCookieBuilder baseCookieBuilder(String value, Duration maxAge) {
        ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from(COOKIE_NAME, value)
                .httpOnly(true)
                .secure(config.cookieSecure())
                .path(config.cookiePath())
                .sameSite(config.cookieSameSite())
                .maxAge(maxAge);

        if (StringUtils.hasText(config.cookieDomain())) {
            builder.domain(config.cookieDomain());
        }

        return builder;
    }
}
