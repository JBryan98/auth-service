package com.jbryan98.authservice.authentication.application;

import com.jbryan98.authservice.authentication.api.dto.AuthenticationRequest;
import com.jbryan98.authservice.authentication.application.dto.AuthenticationResult;
import com.jbryan98.authservice.authentication.application.dto.RefreshTokenResult;

public interface AuthenticationService {
    AuthenticationResult authenticate(AuthenticationRequest request);
    RefreshTokenResult refreshToken(String refreshToken);
    void logout(String refreshToken);
}
