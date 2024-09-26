package com.group1.Care_Koi_System.exceptionhandler.token;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ErrorResponseUtil {
    public static ResponseEntity<String> createErrorResponse(HttpStatus status, String message) {
        String jsonResponse = String.format("{\"error\": \"%s\"}", message);
        return ResponseEntity.status(status).body(jsonResponse);
    }
}
