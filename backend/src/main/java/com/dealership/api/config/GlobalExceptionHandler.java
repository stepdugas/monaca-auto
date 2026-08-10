package com.dealership.api.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * Turn Spring's default error dispatch (which becomes a 403 with empty body
 * because /error isn't in SecurityConfig's permitAll list) into proper 4xx
 * responses with a useful body. Without this, any deserialization failure or
 * DB constraint violation looks like an auth problem to the frontend.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /** Jackson couldn't parse the body — e.g. "" sent for an Integer field. */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleUnreadable(HttpMessageNotReadableException e) {
        log.warn("Request body not readable: {}", e.getMostSpecificCause().getMessage());
        return ResponseEntity.badRequest().body(Map.of(
            "error", "Invalid request body",
            "detail", e.getMostSpecificCause().getMessage()
        ));
    }

    /** @Valid failed on a @RequestBody. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException e) {
        Map<String, String> fieldErrors = new java.util.HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(err ->
            fieldErrors.put(err.getField(), err.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(Map.of(
            "error", "Validation failed",
            "fields", fieldErrors
        ));
    }

    /** DB constraint violation — unique index, not-null, foreign key, etc. */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleIntegrity(DataIntegrityViolationException e) {
        String rootMsg = e.getMostSpecificCause().getMessage();
        log.warn("Data integrity violation: {}", rootMsg);
        String userMsg = rootMsg != null && rootMsg.toLowerCase().contains("vin")
            ? "That VIN is already used by another vehicle. Leave it blank or pick a unique one."
            : "This record conflicts with existing data (duplicate value or missing required field).";
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
            "error", userMsg,
            "detail", rootMsg == null ? "" : rootMsg
        ));
    }
}
