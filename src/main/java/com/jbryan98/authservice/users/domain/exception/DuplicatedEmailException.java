package com.jbryan98.authservice.users.domain.exception;

public class DuplicatedEmailException extends RuntimeException {
    public final String code = "DUPLICATED_EMAIL";
    public DuplicatedEmailException(String email) {
        super("Email '" + email + "' is already taken.");
    }
}
