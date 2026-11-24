package com.jbryan98.authservice.users.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ChangePasswordRequest (
        @NotNull(message = "New password is mandatory")
        @Pattern(
                regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$",
                message = "Password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character."
        )
        String newPassword,

        @NotNull(message = "Current password is mandatory")
        @Pattern(
                regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$",
                message = "Password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character."
        )
        String currentPassword
){
}
