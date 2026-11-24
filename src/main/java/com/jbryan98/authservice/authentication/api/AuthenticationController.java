package com.jbryan98.authservice.authentication.api;

import com.jbryan98.authservice.authentication.api.dto.AuthenticationRequest;
import com.jbryan98.authservice.authentication.api.dto.AuthenticationResponse;
import com.jbryan98.authservice.authentication.api.dto.RefreshTokenRequest;
import com.jbryan98.authservice.authentication.api.dto.TokenResponse;
import com.jbryan98.authservice.authentication.application.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/login")
    @Operation(summary = "Login to generate an access token")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @Valid AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Generate a new access token using a refresh token")
    public ResponseEntity<TokenResponse> refreshToken(@RequestBody @Valid RefreshTokenRequest request) {
        return ResponseEntity.ok(service.refreshToken(request));
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout and revoke the refresh token")
    public ResponseEntity<Void> logout(@RequestBody @Valid RefreshTokenRequest request) {
        service.logout(request);
        return ResponseEntity.noContent().build();
    }
}
