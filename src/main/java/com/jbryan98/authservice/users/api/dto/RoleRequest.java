package com.jbryan98.authservice.users.api.dto;

import jakarta.validation.constraints.Pattern;

public record RoleRequest(
        @Pattern(
                regexp = "^ROLE_[A-Z0-9]+(_[A-Z0-9]+)*$",
                message = "Role name must start with 'ROLE_' and contain only uppercase letters and underscores"
        )
        String name
) {
}
