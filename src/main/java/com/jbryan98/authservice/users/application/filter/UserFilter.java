package com.jbryan98.authservice.users.application.filter;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record UserFilter(
        @Schema(
                description = "Search term to filter users by first name, last name or username. Minimum length is 3 characters.",
                example = "Bryan"
        )
        String search,

        @Schema(
                description = "Filter users by their enabled status. By default, it will return all users regardless of their enabled status.",
                example = "true"
        )
        Boolean enabled,

        @Schema(
                description = "Filter users by their role IDs. It will return users that have at least one of the specified roles.",
                example = "3"
        )
        List<Long> roleIds
) {
}
