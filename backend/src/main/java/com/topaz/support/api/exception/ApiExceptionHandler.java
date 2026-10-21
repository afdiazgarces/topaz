package com.topaz.support.api.exception;

import com.topaz.support.api.dto.ErrorResponse;
import com.topaz.support.domain.exception.DuplicateActiveOrderException;
import com.topaz.support.domain.exception.InvalidStatusTransitionException;
import com.topaz.support.domain.exception.OrderNotFoundException;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class ApiExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(OrderNotFoundException.class)
    ResponseEntity<ErrorResponse> notFound(OrderNotFoundException e) {
        return build(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler({DuplicateActiveOrderException.class, InvalidStatusTransitionException.class})
    ResponseEntity<ErrorResponse> conflict(RuntimeException e) {
        return build(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorResponse> invalid(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return build(HttpStatus.BAD_REQUEST, msg);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    ResponseEntity<ErrorResponse> unreadable(Exception e) {
        return build(HttpStatus.BAD_REQUEST, "Malformed request or invalid enum value");
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus status, String message) {
        log.warn("Request rejected: {} {}", status.value(), message);
        return ResponseEntity.status(status).body(new ErrorResponse(status.value(), message));
    }
}
