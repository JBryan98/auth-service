package com.jbryan98.authservice.users.domain.exception;

public class UserNotFoundException extends RuntimeException {
    public final String code = "USER_NOT_FOUND";
    public UserNotFoundException(String message) {
        super(message);
    }
}
