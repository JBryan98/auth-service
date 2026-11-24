package com.jbryan98.authservice.authentication.application.impl;

import com.jbryan98.authservice.authentication.application.RefreshTokenService;
import com.jbryan98.authservice.authentication.domain.entity.RefreshToken;
import com.jbryan98.authservice.authentication.domain.exception.InvalidRefreshTokenException;
import com.jbryan98.authservice.authentication.domain.repository.RefreshTokenRepository;
import com.jbryan98.authservice.users.domain.exception.UserNotFoundException;
import com.jbryan98.authservice.users.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {
    @Value("${application.security.refresh-token.expiration-days}")
    private long expirationDays;
    private final RefreshTokenRepository repository;
    private final UserRepository userRepository;

    @Override
    public RefreshToken create(String username) {
        log.info("create username: {}", username);
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
        var refreshToken = RefreshToken.create(user, expirationDays);
        return repository.save(refreshToken);
    }

    @Override
    @Transactional(readOnly = true)
    public RefreshToken verify(String refreshToken) {
        log.info("verify refreshToken: {}", refreshToken);
        var dbRefreshToken = repository.findByToken(refreshToken)
                .orElseThrow(() -> new InvalidRefreshTokenException("The provided refresh token is invalid"));
        if(dbRefreshToken.isRevoked()) {
            throw new InvalidRefreshTokenException("The provided refresh token has been revoked");
        }
        if(dbRefreshToken.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new InvalidRefreshTokenException("The provided refresh token has expired");
        }
        return dbRefreshToken;
    }

    @Override
    public RefreshToken rotate(RefreshToken oldRefreshToken) {
        log.info("rotate refreshToken: {}", oldRefreshToken.getToken());
        oldRefreshToken.revoke();
        repository.save(oldRefreshToken);
        var newRefreshToken = RefreshToken.create(oldRefreshToken.getUser(), expirationDays);;
        return repository.save(newRefreshToken);
    }

    @Override
    public void revoke(String refreshToken) {
        log.info("revoke refreshToken: {}", refreshToken);
        var dbRefreshToken = repository.findByToken(refreshToken)
                .orElseThrow(() -> new InvalidRefreshTokenException("The provided refresh token is invalid"));
        dbRefreshToken.revoke();
        repository.save(dbRefreshToken);
    }
}
