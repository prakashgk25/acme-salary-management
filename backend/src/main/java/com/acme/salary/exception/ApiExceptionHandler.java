package com.acme.salary.exception;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<?> notFound(NotFoundException e) {
        return ResponseEntity.status(404).body(java.util.Map.of("error", e.getMessage()));
    }

    @ExceptionHandler({ ConflictException.class, ObjectOptimisticLockingFailureException.class })
    ResponseEntity<?> conflict(Exception e) {
        return ResponseEntity.status(409)
                .body(java.util.Map.of("error", "Record was changed by another user. Refresh and retry."));
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<?> bad(Exception e) {
        return ResponseEntity.status(400).body(java.util.Map.of("error", e.getMessage()));
    }
}
