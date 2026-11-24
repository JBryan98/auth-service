package com.jbryan98.authservice.users.domain.exception;

public class RoleHasAssignedUsersException extends RuntimeException {
    public final String code = "ROLE_HAS_ASSIGNED_USERS";
    public RoleHasAssignedUsersException(int size) {
        super("The role has " + size + " assigned users and cannot be deleted.");
    }
}
