package com.santos.linkshortener.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FormatoInvalidoException.class)
    public ResponseEntity<?> handleFormatoInvalidoException(FormatoInvalidoException ex) {
        return ResponseEntity.badRequest().body(
                Map.of("erro", ex.getMessage())
        );
    }

    @ExceptionHandler(LinkCurtoInvalidoException.class)
    public ResponseEntity<?> handleLinkCurtoInvalidoException(LinkCurtoInvalidoException ex) {
        return ResponseEntity.badRequest().body(
                Map.of("erro", ex.getMessage())
        );
    }
}
