package com.jbryan98.authservice.users.domain.exception;

public class DuplicatedUsernameException extends RuntimeException {
    public final String code = "DUPLICATED_USERNAME";
    public DuplicatedUsernameException(String username) {
        super("Username '" + username + "' is already taken.");
    }
}
