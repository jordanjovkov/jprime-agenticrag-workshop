package io.jprime.agenticrag.ingestor.web.advice;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for all REST controllers in rag-ingestor.
 * <p>
 * Maps application exceptions to appropriate HTTP status codes and human-readable
 * response bodies. Keeps controller code clean by centralizing error handling in one place.
 * <p>
 * Handled exceptions and their HTTP mappings:
 * <ul>
 *   <li>{@link ConstraintViolationException} → {@code 400 Bad Request} (invalid request parameters)</li>
 *   <li>{@link IllegalArgumentException} → {@code 400 Bad Request} (invalid argument values)</li>
 *   <li>{@link RuntimeException} → {@code 500 Internal Server Error} (catch-all)</li>
 * </ul>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        log.warn("Constraint violation: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Invalid request parameter: " + e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("Invalid request parameter: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Invalid request parameter: " + e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        log.error("Operation failed: {}", e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Operation failed: " + e.getMessage());
    }
}
