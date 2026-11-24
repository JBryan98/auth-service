package com.jbryan98.authservice.users.domain.exception;

public class RoleNotFoundException extends RuntimeException {
    public final String code = "ROLE_NOT_FOUND";
    public RoleNotFoundException(String message) {
        super(message);
    }
}
