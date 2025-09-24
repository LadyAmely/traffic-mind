package org.shop.traffic.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.shop.traffic.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.time.OffsetDateTime;

@Slf4j
@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    private ResponseEntity<ErrorResponse> handleEntityNotFoundException(
            EntityNotFoundException ex,
            HttpServletRequest request) {
        log.warn("Warning occurred: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = buildErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request.getRequestURI());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    private ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex,
            HttpServletRequest request) {
        log.warn("Warning occured: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request.getRequestURI());
       return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
               .body(errorResponse);
    }

    @ExceptionHandler(IllegalStateException.class)
    private ResponseEntity<ErrorResponse> handleIllegalStateException(
            IllegalStateException ex,
            HttpServletRequest request) {
        log.warn("Warning occured: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request.getRequestURI());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler(AccessDeniedException.class)
    private ResponseEntity<ErrorResponse> handleAccessDeniedException(
            AccessDeniedException ex,
            HttpServletRequest request) {
        log.warn("Warning occured: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = buildErrorResponse(
                HttpStatus.FORBIDDEN,
                ex.getMessage(),
                request.getRequestURI());
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(errorResponse);
    }

    private ErrorResponse buildErrorResponse(
            HttpStatus status,
            String message,
            String path) {
        return new ErrorResponse(
                OffsetDateTime.now(),
                status.value(),
                status.name(),
                message,
                path
        );
    }
}
