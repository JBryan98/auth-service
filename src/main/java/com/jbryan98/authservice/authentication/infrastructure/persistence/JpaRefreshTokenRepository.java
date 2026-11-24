package com.jbryan98.authservice.authentication.infrastructure.persistence;

import com.jbryan98.authservice.authentication.domain.entity.RefreshToken;
import com.jbryan98.authservice.authentication.domain.repository.RefreshTokenRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface JpaRefreshTokenRepository extends JpaRepository<RefreshToken, Long>, RefreshTokenRepository {
    @Override
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            UPDATE RefreshToken rt
            SET rt.revoked = TRUE
            WHERE rt.user.id = :userId AND rt.revoked = FALSE
            """)
    void revokeAllActiveRefreshTokensByUserId(Long userId);
}
