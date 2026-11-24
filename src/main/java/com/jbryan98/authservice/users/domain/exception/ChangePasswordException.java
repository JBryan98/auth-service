package com.jbryan98.authservice.users.domain.exception;

public class ChangePasswordException extends RuntimeException {
    public final String code = "CHANGE_PASSWORD";
    public ChangePasswordException(String message) {
        super(message);
    }
}
