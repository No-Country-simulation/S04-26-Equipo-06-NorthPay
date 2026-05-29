package org.northpay_contractor_onboarding.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.northpay_contractor_onboarding.dto.ErrorResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApiError.class)
    public ResponseEntity<ErrorResponse> handleCustomApiError(ApiError ex) {

        ErrorResponse errorBody = ErrorResponse.builder()
            .message(ex.getMessage())
            .status(ex.getStatus().value())
            .timestamp(LocalDateTime.now())
            .build();

        return new ResponseEntity<>(errorBody, ex.getStatus());
    }

    /*
     * =========================
     * 400 - BAD REQUEST
     * =========================
     */

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.badRequest().body(ErrorResponse.builder()
                .status(400)
                .error("Bad Request")
                .message("Validation failed")
                .details(errors)
                .timestamp(LocalDateTime.now())
                .build());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex) {

        return ResponseEntity.badRequest().body(ErrorResponse.builder()
                .status(400)
                .error("Bad Request")
                .message("Constraint violation")
                .details(Map.of("message", ex.getMessage()))
                .timestamp(LocalDateTime.now())
                .build());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidJson(
            HttpMessageNotReadableException ex) {

        return ResponseEntity.badRequest().body(ErrorResponse.builder()
                .status(400)
                .error("Bad Request")
                .message("Malformed JSON request")
                .timestamp(LocalDateTime.now())
                .build());
    }

    /*
     * =========================
     * 401 - UNAUTHORIZED
     * =========================
     */

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse.builder()
                .status(401)
                .error("Unauthorized")
                .message("Invalid credentials: " + ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleExpiredJwt(ExpiredJwtException ex) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse.builder()
                .status(401)
                .error("Unauthorized")
                .message(ex.getMessage().isBlank() ? "Token expired" : ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @ExceptionHandler({MalformedJwtException.class, SignatureException.class})
    public ResponseEntity<ErrorResponse> handleInvalidJwt(Exception ex) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse.builder()
                .status(401)
                .error("Unauthorized")
                .message("Invalid token: " + ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponse> handleCustomInvalidToken(InvalidTokenException ex) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse.builder()
                .status(401)
                .error("Unauthorized")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build());
    }

    /*
     * =========================
     * 403 - FORBIDDEN
     * =========================
     */

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorResponse.builder()
                .status(403)
                .error("Forbidden")
                .message("You do not have permission to access this resource. " + ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build());
    }

    /*
     * =========================
     * 404 - NOT FOUND
     * =========================
     */

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(NotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.builder()
                .status(404)
                .error("Not Found")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build());
    }

    /*
     * =========================
     * 409 - CONFLICT
     * =========================
     */

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse.builder()
                .status(409)
                .error("Conflict")
                .message("Database integrity violation")
                .timestamp(LocalDateTime.now())
                .stackTrace(ex.getStackTrace())
                .build());
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyExists(AlreadyExistsException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse.builder()
                .status(409)
                .error("Conflict")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build());
    }

    /*
     * =========================
     * 405 - METHOD NOT ALLOWED
     * =========================
     */

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(ErrorResponse.builder()
                .status(405)
                .error("Method Not Allowed")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build());
    }

    /**
     * =========================
     * 502 - BAD GATEWAY (Errors from external services)
     * =========================
     */
    @ExceptionHandler(CustomMailException.class)
    public ResponseEntity<ErrorResponse> handleCustomMailException(CustomMailException ex) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(ErrorResponse.builder()
                .status(502)
                .error("Bad Gateway")
                .message("Error from external service. " + ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build());
    }

    /*
     * =========================
     * 500 - INTERNAL SERVER ERROR
     * =========================
     */

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponse.builder()
                .status(500)
                .error("Internal Server Error")
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .stackTrace(ex.getStackTrace())
                .build());
    }
}
