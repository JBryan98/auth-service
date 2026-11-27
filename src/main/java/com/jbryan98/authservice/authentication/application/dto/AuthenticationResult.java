package com.jbryan98.authservice.authentication.application.dto;

import com.jbryan98.authservice.authentication.api.dto.AuthenticationResponse;

public record AuthenticationResult(
        String refreshToken,
        AuthenticationResponse body
) {
}
