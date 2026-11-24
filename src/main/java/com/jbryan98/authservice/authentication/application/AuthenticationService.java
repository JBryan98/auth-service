package com.jbryan98.authservice.authentication.application;

import com.jbryan98.authservice.authentication.api.dto.AuthenticationRequest;
import com.jbryan98.authservice.authentication.api.dto.AuthenticationResponse;
import com.jbryan98.authservice.authentication.api.dto.RefreshTokenRequest;
import com.jbryan98.authservice.authentication.api.dto.TokenResponse;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);
    TokenResponse refreshToken(RefreshTokenRequest request);
    void logout(RefreshTokenRequest request);
}
