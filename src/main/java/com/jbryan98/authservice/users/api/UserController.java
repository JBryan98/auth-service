package com.jbryan98.authservice.users.api;

import com.jbryan98.authservice.users.api.dto.ChangePasswordRequest;
import com.jbryan98.authservice.users.api.dto.CreateUserRequest;
import com.jbryan98.authservice.users.api.dto.RestoreUserPasswordRequest;
import com.jbryan98.authservice.users.api.dto.UpdateUserRequest;
import com.jbryan98.authservice.users.api.dto.UserResponse;
import com.jbryan98.authservice.users.application.UserService;
import com.jbryan98.authservice.users.application.filter.UserFilter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@Tag(
        name = "User",
        description = "API for user management. Access is restricted based on user roles (ADMIN and USER)."
)
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    @Operation(summary = "Get a list of paginated users.")
    public ResponseEntity<Page<UserResponse>> findAll(
            @ParameterObject @PageableDefault Pageable pageable,
            @ParameterObject UserFilter filter
    ) {
        return ResponseEntity.ok(service.findAll(pageable, filter));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @Operation(summary = "Get a user by its ID.")
    public ResponseEntity<UserResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findDto(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new user.")
    public ResponseEntity<UserResponse> create(@RequestBody @Valid CreateUserRequest request) {
        return new ResponseEntity<>(service.create(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @Operation(summary = "Update an existing user by its ID.")
    public ResponseEntity<UserResponse> update(@PathVariable Long id, @RequestBody @Valid UpdateUserRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @PatchMapping("/{id}/change-password")
    @PreAuthorize("hasRole('USER') and #id == authentication.principal.id")
    @Operation(summary = "Change the password of an existing user by its ID.")
    public ResponseEntity<Void> changePassword(@PathVariable Long id, @RequestBody @Valid ChangePasswordRequest request) {
        service.changePassword(id, request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/reset-password")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Reset the password of an existing user.")
    public ResponseEntity<Void> resetPassword(@PathVariable Long id, @RequestBody @Valid RestoreUserPasswordRequest request) {
        service.resetPassword(id, request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/enable")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Enable an existing user.")
    public ResponseEntity<Void> enable(@PathVariable Long id) {
        service.enable(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/disable")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Disable an existing user.")
    public ResponseEntity<Void> disable(@PathVariable Long id) {
        service.disable(id);
        return ResponseEntity.ok().build();
    }
}
