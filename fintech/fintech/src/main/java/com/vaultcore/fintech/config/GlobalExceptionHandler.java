package com.vaultcore.fintech.config;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle generic runtime exceptions
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntime(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }

    // Handle invalid arguments (e.g. validation errors)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldError() != null
                ? ex.getBindingResult().getFieldError().getDefaultMessage()
                : "Validation error";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", message));
    }

    // Handle unauthorized access
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<?> handleSecurity(SecurityException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Unauthorized: " + ex.getMessage()));
    }

    // Catch-all fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {
    	ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Unexpected error occurred"));
        
        

    }
}
