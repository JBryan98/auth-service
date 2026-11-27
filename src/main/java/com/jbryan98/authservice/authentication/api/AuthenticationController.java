package com.jbryan98.authservice.authentication.api;

import com.jbryan98.authservice.authentication.api.cookie.RefreshTokenCookieService;
import com.jbryan98.authservice.authentication.api.dto.AuthenticationRequest;
import com.jbryan98.authservice.authentication.api.dto.AuthenticationResponse;
import com.jbryan98.authservice.authentication.api.dto.TokenResponse;
import com.jbryan98.authservice.authentication.application.AuthenticationService;
import com.jbryan98.authservice.authentication.domain.exception.InvalidRefreshTokenException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(
        name = "Authentication",
        description = "Authentication API responsible for user login and token generation. "
                + "Provides access tokens used to authenticate subsequent requests."
)
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;
    private final RefreshTokenCookieService refreshTokenCookieService;

    @PostMapping("/login")
    @Operation(summary = "Login to generate an access token, refresh token and user profile information. The refresh token is stored in a secure HTTP-only cookie.")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @Valid AuthenticationRequest request
    ) {
        var result = service.authenticate(request);
        var cookie = refreshTokenCookieService.create(result.refreshToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(result.body());
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Generate a new access token using a refresh token")
    public ResponseEntity<TokenResponse> refreshToken(HttpServletRequest request) {
        var refreshToken = refreshTokenCookieService.extractRefreshToken(request)
                .orElseThrow(() -> new InvalidRefreshTokenException("Refresh token not found in cookies"));
        var result = service.refreshToken(refreshToken);
        var cookie = refreshTokenCookieService.create(result.refreshToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(result.body());
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout and revoke the refresh token")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        refreshTokenCookieService.extractRefreshToken(request)
                .ifPresent(service::logout);
        var clearedCookie = refreshTokenCookieService.clear();
        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, clearedCookie.toString())
                .build();
    }
}
