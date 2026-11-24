package com.jbryan98.authservice.authentication.domain.exception;

public class InvalidRefreshTokenException extends RuntimeException {
    public final String code = "INVALID_REFRESH_TOKEN";
    public InvalidRefreshTokenException(String message) {
        super(message);
    }
}
