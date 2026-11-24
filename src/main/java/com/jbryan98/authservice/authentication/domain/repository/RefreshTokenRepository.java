package com.jbryan98.authservice.authentication.domain.repository;

import com.jbryan98.authservice.authentication.domain.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository {
    RefreshToken save(RefreshToken refreshToken);
    Optional<RefreshToken> findByToken(String token);
    void revokeAllActiveRefreshTokensByUserId(Long userId);
}