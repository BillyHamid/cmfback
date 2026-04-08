package com.adouk.finacombackend.infrastructure.rest.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class Handler {

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Map<Object, Object>> handleExpiredJwtException(ExpiredJwtException ex) {
        Map<Object, Object> response = new HashMap<>();
        response.put("status", false);
        response.put("message", ex.getMessage());
        response.put("payload", null);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}
