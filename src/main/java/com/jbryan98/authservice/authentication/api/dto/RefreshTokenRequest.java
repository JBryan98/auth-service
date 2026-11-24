package com.jbryan98.authservice.authentication.api.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(
        @NotBlank(message = "Refresh token is mandatory.")
        String refreshToken
) {
}
