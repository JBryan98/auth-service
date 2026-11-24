package com.jbryan98.authservice.authentication.api.dto;

import java.util.List;

public record AuthenticationResponse(
        TokenResponse token,
        Profile profile
) {
    public record Profile(
            Long id,
            String email,
            String firstName,
            String lastName,
            List<String> roles
    ) {
    }
}
