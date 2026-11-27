package com.jbryan98.authservice.authentication.api.cookie;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;

import java.util.Optional;

public interface RefreshTokenCookieService {
    String COOKIE_NAME = "refresh_token";

    ResponseCookie create(String refreshToken);
    ResponseCookie clear();
    Optional<String> extractRefreshToken(HttpServletRequest request);
}
