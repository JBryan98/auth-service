package com.jbryan98.authservice.authentication.application.impl;

import com.jbryan98.authservice.authentication.api.dto.AuthenticationRequest;
import com.jbryan98.authservice.authentication.api.dto.AuthenticationResponse;
import com.jbryan98.authservice.authentication.api.dto.TokenResponse;
import com.jbryan98.authservice.authentication.application.AuthenticationService;
import com.jbryan98.authservice.authentication.application.RefreshTokenService;
import com.jbryan98.authservice.authentication.application.dto.AuthenticationResult;
import com.jbryan98.authservice.authentication.application.dto.RefreshTokenResult;
import com.jbryan98.authservice.authentication.infrastructure.security.JwtService;
import com.jbryan98.authservice.users.domain.entity.Role;
import com.jbryan98.authservice.users.domain.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Override
    public AuthenticationResult authenticate(AuthenticationRequest rq) {
        log.info("authenticate {}", rq.username());
        Authentication auth;
        try {
            auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            rq.username(),
                            rq.password()
                    )
            );
        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException("The username or password is incorrect.");
        } catch (DisabledException ex) {
            throw new DisabledException("The user account is disabled.");
        }
        var claims = new HashMap<String, Object>();
        var user = (User) auth.getPrincipal();
        claims.put("username", user.getUsername());
        var jwtToken = jwtService.generateToken(claims, user);
        var refreshToken = refreshTokenService.create(user.getUsername());
        var body =  new AuthenticationResponse(
                TokenResponse.bearer(jwtToken),
                new AuthenticationResponse.Profile(
                        user.getId(),
                        user.getEmail(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getRoles().stream().map(Role::getName).toList()
                )
        );
        return new AuthenticationResult(refreshToken.getToken(), body);
    }

    @Override
    public RefreshTokenResult refreshToken(String refreshToken) {
        var verifiedToken = refreshTokenService.verify(refreshToken);
        var user = verifiedToken.getUser();
        var claims = new HashMap<String, Object>();
        claims.put("username", user.getUsername());
        var jwtToken = jwtService.generateToken(claims, user);
        return new RefreshTokenResult(refreshToken, TokenResponse.bearer(jwtToken));
    }

    @Override
    public void logout(String refreshToken) {
        refreshTokenService.revoke(refreshToken);
    }
}
