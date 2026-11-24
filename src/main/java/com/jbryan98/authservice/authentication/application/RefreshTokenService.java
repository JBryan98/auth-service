package com.jbryan98.authservice.authentication.application;

import com.jbryan98.authservice.authentication.domain.entity.RefreshToken;
import com.jbryan98.authservice.authentication.domain.exception.InvalidRefreshTokenException;
import com.jbryan98.authservice.users.domain.exception.UserNotFoundException;

public interface RefreshTokenService {
    /**
     * Creates and persists a new refresh token for the given username.
     * @param username the username for which the refresh token is to be created
     * @throws UserNotFoundException if the user with the given username does not exist
     * @return the created RefreshToken
     */
    RefreshToken create(String username);

    /**
     * Validates the given refresh token and returns the corresponding RefreshToken entity if valid.
     * @param refreshToken the raw refresh token
     * @throws InvalidRefreshTokenException if the refresh token is invalid, revoked, or expired
     * @return the persisted RefreshToken entity corresponding to the given token
     */
    RefreshToken verify(String refreshToken);

    /**
     * Rotates the given refresh token by revoking it and creating a new one for the same user.
     * @param oldRefreshToken the existing RefreshToken to be rotated
     * @throws InvalidRefreshTokenException if the old refresh token is invalid, revoked, or expired
     * @return the newly created RefreshToken
     */
    RefreshToken rotate(RefreshToken oldRefreshToken);

    /**
     * Revokes the given refresh token, marking it as invalid for future use.
     * @param refreshToken the raw refresh token to be revoked
     * @throws InvalidRefreshTokenException if the refresh token is invalid
     */
    void revoke(String refreshToken);
}
