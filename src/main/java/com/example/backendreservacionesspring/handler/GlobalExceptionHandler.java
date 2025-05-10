
package com.example.backendreservacionesspring.handler;

import com.example.backendreservacionesspring.dto.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ApiResponse<String> handleAccessDeniedException(AccessDeniedException ex) {
        return new ApiResponse<>(
                HttpStatus.FORBIDDEN.value(),
                null,
                "Acceso denegado: No tiene permisos para realizar esta acción"
        );
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ApiResponse<String> handleEntityNotFound(EntityNotFoundException ex) {
        return new ApiResponse<>(HttpStatus.NOT_FOUND.value(), null, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    public ApiResponse<String> handleAuthenticationException(AuthenticationException ex) {
        return new ApiResponse<>(
                HttpStatus.UNAUTHORIZED.value(),
                null,
                "No autorizado: Se requiere autenticación"
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), errors, "Error de validación");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ApiResponse<String> handleAllExceptions(Exception ex) {
        return new ApiResponse<>(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                null,
                "Error interno del servidor: " + ex.getMessage()
        );
    }
}