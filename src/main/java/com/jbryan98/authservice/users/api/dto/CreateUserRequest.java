package com.jbryan98.authservice.users.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import java.util.Set;

public record CreateUserRequest(
        @NotBlank(message = "First name is mandatory")
        String firstName,

        @NotBlank(message = "Last name is mandatory")
        String lastName,

        @NotBlank(message = "Email is mandatory")
        @Email(message = "Email is incorrect")
        String email,

        @NotBlank(message = "Username is mandatory")
        String username,

        @NotBlank(message = "Password is mandatory")
        @Pattern(
                regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$",
                message = "Password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character."
        )
        String password,

        @NotEmpty(message = "You should provide at least one role id in the array")
        Set<Long> roleIds
) {
    private static final String MASK = "*****";
    @Override
    public String toString() {
        return "CreateUserRequest{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + maskEmail(email) + '\'' +
                ", username='" + maskUsername(username) + '\'' +
                ", roleIds=" + roleIds +
                '}';
    }

    private static String maskUsername(String username) {
        if (username.length() == 1) {
            return MASK;
        }
        return username.charAt(0) + MASK + username.charAt(username.length() - 1);
    }

    private static String maskEmail(String email) {
        int atIndex = email.indexOf('@');
        return email.charAt(0) + MASK + email.substring(atIndex);
    }
}
