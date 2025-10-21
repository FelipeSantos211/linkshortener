package com.santos.linkshortener.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * Handler global de exceções para a aplicação.
 * Intercepta exceções e retorna respostas HTTP formatadas.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Trata exceções de formato inválido.
     * 
     * @param ex Exceção de formato inválido
     * @return ResponseEntity com status 400 e mensagem de erro
     */
    @ExceptionHandler(FormatoInvalidoException.class)
    public ResponseEntity<?> handleFormatoInvalidoException(FormatoInvalidoException ex) {
        return ResponseEntity.badRequest().body(
                Map.of("erro", ex.getMessage())
        );
    }

    /**
     * Trata exceções de link curto inválido.
     * 
     * @param ex Exceção de link curto inválido
     * @return ResponseEntity com status 400 e mensagem de erro
     */
    @ExceptionHandler(LinkCurtoInvalidoException.class)
    public ResponseEntity<?> handleLinkCurtoInvalidoException(LinkCurtoInvalidoException ex) {
        return ResponseEntity.badRequest().body(
                Map.of("erro", ex.getMessage())
        );
    }
}
