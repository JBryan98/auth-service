package com.jbryan98.authservice.users.domain.exception;

public class DuplicatedRoleException extends RuntimeException {
    final String code = "DUPLICATED_ROLE";
    public DuplicatedRoleException(String name) {
        super("Role '" + name + "' already exists.");
    }
}
