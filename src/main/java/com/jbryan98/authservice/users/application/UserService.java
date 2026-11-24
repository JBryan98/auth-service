package com.jbryan98.authservice.users.application;


import com.jbryan98.authservice.users.api.dto.ChangePasswordRequest;
import com.jbryan98.authservice.users.api.dto.CreateUserRequest;
import com.jbryan98.authservice.users.api.dto.RestoreUserPasswordRequest;
import com.jbryan98.authservice.users.api.dto.UpdateUserRequest;
import com.jbryan98.authservice.users.api.dto.UserResponse;
import com.jbryan98.authservice.users.application.filter.UserFilter;
import com.jbryan98.authservice.users.domain.entity.User;
import com.jbryan98.authservice.users.domain.exception.ChangePasswordException;
import com.jbryan98.authservice.users.domain.exception.DuplicatedEmailException;
import com.jbryan98.authservice.users.domain.exception.DuplicatedUsernameException;
import com.jbryan98.authservice.users.domain.exception.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;

public interface UserService {
    /**
     * Returns a paginated list of users.
     *
     * @param pageable pagination information
     * @param filter  user filter criteria
     * @return page of UserResponse
     */
    Page<UserResponse> findAll(Pageable pageable, UserFilter filter);

    /**
     * Finds an user by its id.
     *
     * @param id user identifier
     * @return User entity
     * @throws UserNotFoundException if the user does not exist
     */
    User find(Long id);

    /**
     * Finds an user by its id.
     *
     * @param id user identifier
     * @return UserResponse DTO
     * @throws UserNotFoundException if the user does not exist
     */
    UserResponse findDto(Long id);

    /**
     * Creates a new user.
     *
     * @param request user data
     * @return created UserResponse DTO
     * @throws DuplicatedUsernameException if the username already exists
     * @throws DuplicatedEmailException    if the email already exists
     */
    UserResponse create(CreateUserRequest request);

    /**
     * Updates an existing user
     *
     * @param id      user identifier
     * @param request updated data
     * @return updated UserResponse DTO
     * @throws DuplicatedUsernameException if the updated username already exists
     * @throws DuplicatedEmailException    if the updated email already exists
     * @throws UserNotFoundException       if the user does not exist
     */
    UserResponse update(Long id, UpdateUserRequest request);

    /**
     * Returns the current user authenticated
     *
     * @return User DTO
     * @throws AuthenticationCredentialsNotFoundException if the user is not authenticated
     */
    UserResponse findCurrentUser();

    /**
     * Updates the password of an existing user.
     *
     * @param id      user identifier
     * @param request updated data
     * @throws AccessDeniedException   if a different user tries to change the password of another user
     * @throws BadCredentialsException if the current password is incorrect
     * @throws ChangePasswordException if the new password is the same as the current password
     */
    void changePassword(Long id, ChangePasswordRequest request);

    /**
     * Resets the password of an existing user.
     * This action is exclusive for ADMIN users.
     *
     * @param id      user identifier
     * @param request reset password data
     * @throws AccessDeniedException if a non-admin user tries to reset the password of another user
     */
    void resetPassword(Long id, RestoreUserPasswordRequest request);

    /**
     * Enables an existing user.
     *
     * @param id User identifier
     */
    void enable(Long id);

    /**
     * Disables an existing user.
     * This action will also revoke all active refresh tokens associated with the user.
     *
     * @param id User identifier
     */
    void disable(Long id);
}
