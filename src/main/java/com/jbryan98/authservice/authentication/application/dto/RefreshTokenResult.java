package com.jbryan98.authservice.authentication.application.dto;

import com.jbryan98.authservice.authentication.api.dto.TokenResponse;

public record RefreshTokenResult(
        String refreshToken,
        TokenResponse body
) {
}
