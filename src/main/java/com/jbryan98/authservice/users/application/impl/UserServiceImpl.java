package com.jbryan98.authservice.users.application.impl;


import com.jbryan98.authservice.authentication.domain.repository.RefreshTokenRepository;
import com.jbryan98.authservice.users.api.dto.ChangePasswordRequest;
import com.jbryan98.authservice.users.api.dto.CreateUserRequest;
import com.jbryan98.authservice.users.api.dto.RestoreUserPasswordRequest;
import com.jbryan98.authservice.users.api.dto.UpdateUserRequest;
import com.jbryan98.authservice.users.api.dto.UserResponse;
import com.jbryan98.authservice.users.api.mapper.UserMapper;
import com.jbryan98.authservice.users.application.RoleService;
import com.jbryan98.authservice.users.application.UserService;
import com.jbryan98.authservice.users.application.filter.UserFilter;
import com.jbryan98.authservice.users.domain.entity.Role;
import com.jbryan98.authservice.users.domain.entity.RoleName;
import com.jbryan98.authservice.users.domain.entity.User;
import com.jbryan98.authservice.users.domain.exception.ChangePasswordException;
import com.jbryan98.authservice.users.domain.exception.DuplicatedEmailException;
import com.jbryan98.authservice.users.domain.exception.DuplicatedUsernameException;
import com.jbryan98.authservice.users.domain.exception.UserNotFoundException;
import com.jbryan98.authservice.users.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> findAll(Pageable pageable, UserFilter filter) {
        log.info("findAll pageable: {} filter: {}", pageable, filter);
        return repository.findAll(pageable, filter).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public User find(Long id) {
        log.info("findByIdOrThrowException {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id '" + id + "'."));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse findDto(Long id) {
        log.info("findById {}", id);
        var user = find(id);
        return mapper.toResponse(user);
    }

    @Override
    public UserResponse create(CreateUserRequest request) {
        log.info("create request {}", request);
        validateUqUsername(request.username());
        validateUqEmail(request.email());
        var roles = getUserRoles(request.roleIds());
        var encodedPassword = passwordEncoder.encode(request.password());
        var user = User.create(
                request.firstName(),
                request.lastName(),
                request.email(),
                request.username(),
                encodedPassword,
                roles
        );
        repository.save(user);
        return mapper.toResponse(user);
    }

    @Override
    public UserResponse update(Long id, UpdateUserRequest request) {
        log.info("update id {} request {}", id, request);
        var user = find(id);
        if (!user.getEmail().equals(request.email())) {
            validateUqEmail(request.email());
        }
        // Only admin users can update roles
        var roles = user.getRoles();
        var currentUser = findCurrentUser();
        if (currentUser.admin()) {
            roles = getUserRoles(request.roleIds());
        }
        user.update(
                request.firstName(),
                request.lastName(),
                request.email(),
                roles
        );
        var updatedUser = repository.save(user);
        return mapper.toResponse(updatedUser);
    }

    @Override
    public UserResponse findCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new AuthenticationCredentialsNotFoundException("User is not authenticated");
        }
        var user = (User) authentication.getPrincipal();
        return mapper.toResponse(user);
    }

    @Override
    public void changePassword(Long id, ChangePasswordRequest request) {
        log.info("changePassword id {}", id);
        var currentUser = findCurrentUser();
        if (!currentUser.id().equals(id)) {
            throw new AccessDeniedException("You are not allowed to change this password");
        }
        var user = find(id);
        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new BadCredentialsException("Current password is incorrect");
        }
        var newPassword = request.newPassword();
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new ChangePasswordException("New password must be different from the current password");
        }
        user.changePassword(passwordEncoder.encode(newPassword));
        repository.save(user);
    }

    @Override
    public void resetPassword(Long id, RestoreUserPasswordRequest request) {
        log.info("resetPassword id {}", id);
        var user = find(id);
        user.changePassword(passwordEncoder.encode(request.newPassword()));
        repository.save(user);
    }

    @Override
    public void enable(Long id) {
        log.info("enable id {}", id);
        var user = find(id);
        user.enable();
        repository.save(user);
    }

    @Override
    public void disable(Long id) {
        log.info("disable id {}", id);
        var user = find(id);
        user.disable();
        refreshTokenRepository.revokeAllActiveRefreshTokensByUserId(id);
        repository.save(user);
    }

    private List<Role> getUserRoles(Set<Long> roleIds) {
        log.info("getUserRoles roleIds {}", roleIds);
        var roles = new ArrayList<Role>();
        // Ensure the default role (ROLE_USER) is included in the roles list, even if not provided in the request
        var defaultRole = roleService.findByName(RoleName.ROLE_USER.name());
        roles.add(defaultRole);
        var additionalRoles = roleIds.stream()
                .filter(roleId -> !roleId.equals(defaultRole.getId()))
                .map(roleService::find)
                .toList();
        roles.addAll(additionalRoles);
        return roles;
    }

    private Optional<User> findByUsername(String username) {
        log.info("findByUsername {}", username);
        return repository.findByUsername(username);
    }

    private Optional<User> findByEmail(String email) {
        log.info("findByEmail email {}", email);
        return repository.findByEmail(email);
    }

    private void validateUqUsername(String username) {
        log.info("validateUqUsername {}", username);
        var optUser = findByUsername(username);
        if (optUser.isPresent()) {
            throw new DuplicatedUsernameException(username);
        }
    }

    private void validateUqEmail(String email) {
        log.info("validateUqEmail email {}", email);
        var optUser = findByEmail(email);
        if (optUser.isPresent()) {
            throw new DuplicatedEmailException(email);
        }
    }
}
