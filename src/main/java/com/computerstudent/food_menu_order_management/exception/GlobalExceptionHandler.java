package com.computerstudent.food_menu_order_management.exception;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

// automatically handle these below type of exception and sent to response entity
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "error", ex.getMessage(),
                        "timestamp", LocalDateTime.now()
                ));
    }

    @ExceptionHandler(MenuNotFoundException.class)
    public ResponseEntity<?> handleUserNotFound(MenuNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "error", ex.getMessage(),
                        "timestamp", LocalDateTime.now()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<?> handleDuplicateResourceException(DuplicateResourceException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                Map.of(
                        "error", ex.getMessage() != null ? ex.getMessage() : "Something's wrong",
                        "cause", ex.getCause() != null ? ex.getCause().getMessage() : "No root cause",
                        "timestamp", LocalDateTime.now()
                ));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(Exception ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(
                        "details", ex.getMessage(),
                        "cause", ex.getCause() != null ? ex.getCause().getMessage() : "No root cause",
                        "timestamp", LocalDateTime.now()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "error", "An unexpected error occurred",
                        "details", ex.getMessage(),
                        "cause", ex.getCause() != null ? ex.getCause().getMessage() : "No root cause",
                        "timestamp", LocalDateTime.now()
                ));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<?> handleInvalidCredentials(InvalidCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Unauthorized", "message", ex.getMessage()));
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<?> handlePasswordMismatchException(PasswordMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Password mismatch", "message", ex.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleJacksonDeserializationError(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();

        if (cause instanceof UnrecognizedPropertyException unrecognized) {
            String fieldName = unrecognized.getPropertyName();
            String message = "Unrecognized field: \"" + fieldName + "\". Please remove it or correct the name.";
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", message));
        }

        return ResponseEntity
                .badRequest()
                .body(Map.of("error", "Malformed JSON request", "details", ex.getMessage()));
    }
}
