package com.santos.linkshortener.validation.impl;

import com.santos.linkshortener.dto.LoginRequest;
import com.santos.linkshortener.exception.FormatoInvalidoException;
import com.santos.linkshortener.validation.Validar;
import org.springframework.stereotype.Component;

/**
 * Validador para requisições de login.
 * Verifica formato e comprimento de username e password.
 */
@Component
public class ValidaLoginRequestImpl implements Validar<LoginRequest> {

    /**
     * Valida dados de login.
     * 
     * @param request Requisição de login
     * @throws FormatoInvalidoException Se username ou password forem inválidos
     */
    @Override
    public void validar(LoginRequest request) {
        if (request.username() == null || request.username().isBlank()) {
            throw new FormatoInvalidoException("Username é obrigatório.");
        }
        
        if (request.username().length() < 3 || request.username().length() > 50) {
            throw new FormatoInvalidoException("Username deve ter entre 3 e 50 caracteres.");
        }
        
        if (request.password() == null || request.password().isBlank()) {
            throw new FormatoInvalidoException("Password é obrigatório.");
        }
        
        if (request.password().length() < 6) {
            throw new FormatoInvalidoException("Password deve ter no mínimo 6 caracteres.");
        }
    }
}
