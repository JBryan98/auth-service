package com.jbryan98.authservice.users.api.dto;
import java.util.List;

public record UserResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String username,
        String fullName,
        boolean admin,
        List<RoleResponse> roles
) {
}
