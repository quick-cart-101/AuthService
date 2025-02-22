package com.quickcart.authservice.controllers;

import com.quickcart.authservice.exceptions.InvalidUserDetailsException;
import com.quickcart.authservice.exceptions.UserNotRegisteredException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.quickcart.authservice.utils.Constants.*;

@ControllerAdvice
public class GlobalExceptionController {

    private Map<String, Object> createErrorResponse(Exception ex, HttpStatus status, String error) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put(TIMESTAMP, LocalDateTime.now());
        errorResponse.put(STATUS, status.value());
        errorResponse.put(ERROR, error);
        errorResponse.put(MESSAGE, ex.getMessage());
        return errorResponse;
    }

    @ExceptionHandler(UserNotRegisteredException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotRegisteredException(UserNotRegisteredException ex) {
        Map<String, Object> errorResponse = createErrorResponse(ex, HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(InvalidUserDetailsException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidUserDetailsException(InvalidUserDetailsException ex) {
        Map<String, Object> errorResponse = createErrorResponse(ex, HttpStatus.UNAUTHORIZED, ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
        Map<String, Object> errorResponse = createErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}