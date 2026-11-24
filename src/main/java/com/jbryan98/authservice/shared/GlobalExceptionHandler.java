package com.jbryan98.authservice.shared;

import com.jbryan98.authservice.authentication.domain.exception.InvalidRefreshTokenException;
import com.jbryan98.authservice.users.domain.exception.ChangePasswordException;
import com.jbryan98.authservice.users.domain.exception.DuplicatedEmailException;
import com.jbryan98.authservice.users.domain.exception.DuplicatedUsernameException;
import com.jbryan98.authservice.users.domain.exception.RoleHasAssignedUsersException;
import com.jbryan98.authservice.users.domain.exception.RoleNotFoundException;
import com.jbryan98.authservice.users.domain.exception.UserNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    // —————————————————————————————————————  USER EXCEPTIONS———————————————————————————————————————————————————————————
    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
        var body = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        body.setTitle("User Not Found");
        body.setProperty("code", ex.code);
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(DuplicatedEmailException.class)
    protected ResponseEntity<Object> handleDuplicatedEmailException(DuplicatedEmailException ex, WebRequest request) {
        var body = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        body.setTitle("Duplicated Email");
        body.setProperty("code", ex.code);
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(DuplicatedUsernameException.class)
    protected ResponseEntity<Object> handleDuplicatedUsernameException(DuplicatedUsernameException ex, WebRequest request) {
        var body = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        body.setTitle("Duplicated Username");
        body.setProperty("code", ex.code);
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(ChangePasswordException.class)
    protected ResponseEntity<Object> handleChangePasswordException(ChangePasswordException ex, WebRequest request) {
        var body = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        body.setTitle("Change Password Error");
        body.setProperty("code", ex.code);
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    // —————————————————————————————————————  ROLE EXCEPTIONS———————————————————————————————————————————————————————————

    @ExceptionHandler(RoleNotFoundException.class)
    protected ResponseEntity<Object> handleRoleNotFoundException(RoleNotFoundException ex, WebRequest request) {
        var body = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        body.setTitle("Role Not Found");
        body.setProperty("code", ex.code);
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(RoleHasAssignedUsersException.class)
    protected ResponseEntity<Object> handleRoleHasAssignedUsersException(RoleHasAssignedUsersException ex, WebRequest request) {
        var body = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        body.setTitle("Role Has Assigned Users");
        body.setProperty("code", ex.code);
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    // —————————————————————————————————————  AUTHENTICATION EXCEPTIONS—————————————————————————————————————————————————
    @ExceptionHandler(InvalidRefreshTokenException.class)
    protected ResponseEntity<Object> handleInvalidRefreshTokenException(InvalidRefreshTokenException ex, WebRequest request) {
        var body = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
        body.setTitle("Invalid Refresh Token");
        body.setProperty("code", ex.code);
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        var body = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, ex.getMessage());
        body.setTitle("Access Denied");
        body.setProperty("code", "ACCESS_DENIED");
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        var body = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
        body.setTitle("Authentication Failed");
        body.setProperty("code", "BAD_CREDENTIALS");
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<Object> handleDisabledException(DisabledException ex, WebRequest request) {
        var body = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
        body.setTitle("Authentication Failed");
        body.setProperty("code", "USER_DISABLED");
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> handleUsernameNotFoundException(UsernameNotFoundException ex, WebRequest request) {
        var body = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
        body.setTitle("Authentication Failed");
        body.setProperty("code", "USER_NOT_FOUND");
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Object> handleExpiredJwtException(ExpiredJwtException ex, WebRequest request) {
        var body = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "The provided JWT has expired.");
        body.setTitle("JWT Expired");
        body.setProperty("code", "EXPIRED_JWT");
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<Object> handleJwtException(JwtException ex, WebRequest request) {
        var body = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "The provided JWT is invalid.");
        body.setTitle("Invalid JWT");
        body.setProperty("code", "INVALID_JWT");
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    // —————————————————————————————————————  GENERIC EXCEPTIONS————————————————————————————————————————————————————————

    @Override
    protected @Nullable ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        var errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::toPointerError)
                .toList();
        var body = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "The request contains one or more invalid fields.");
        body.setTitle("Validation Error");
        body.setProperty("code", "REQUEST_VALIDATION_ERROR");
        body.setProperty("errors", errors);
        return handleExceptionInternal(ex, body, headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<Object> handleGenericException(RuntimeException ex, WebRequest request) {
        log.error("An unexpected error occurred: {}", ex.getMessage(), ex);
        var body = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred.");
        body.setTitle("Internal Server Error");
        body.setProperty("code", HttpStatus.INTERNAL_SERVER_ERROR.name());
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private Map<String, String> toPointerError(FieldError fieldError) {
        var pointer = "#/" + fieldError.getField().replace(".", "/");
        var detail = fieldError.getDefaultMessage() != null ? fieldError.getDefaultMessage() : "Invalid value";
        return Map.of("pointer", pointer, "detail", detail);
    }
}