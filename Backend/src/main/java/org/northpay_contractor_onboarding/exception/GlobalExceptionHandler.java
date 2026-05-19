package org.northpay_contractor_onboarding.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.northpay_contractor_onboarding.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.AllArgsConstructor;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @AllArgsConstructor
    public class GenericExceptionResponseDTO {
        String message;
        String cause;
        StackTraceElement[] trace;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<GenericExceptionResponseDTO> handleRuntimeExceptions(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new GenericExceptionResponseDTO(ex.getMessage(), ex.getCause().toString(), ex.getStackTrace()));
    }

    @ExceptionHandler(ApiError.class)
    public ResponseEntity<ErrorResponse> handleCustomApiError(ApiError ex) {

        ErrorResponse errorBody = new ErrorResponse(
                ex.getMessage(),
                ex.getStatus().value(),
                LocalDateTime.now());

        return new ResponseEntity<>(errorBody, ex.getStatus());
    }
}