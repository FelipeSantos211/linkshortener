package com.santos.linkshortener.validation.impl;

import com.santos.linkshortener.dto.RegisterRequest;
import com.santos.linkshortener.exception.FormatoInvalidoException;
import com.santos.linkshortener.validation.Validar;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * Validador para requisições de registro de usuário.
 * Verifica username, password e email com regras de formato e segurança.
 */
@Component
public class ValidaRegisterRequestImpl implements Validar<RegisterRequest> {
    
    /** Padrão regex para validação de email */
    private static final Pattern EMAIL_PATTERN = 
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    
    /**
     * Valida dados de registro de usuário.
     * 
     * @param request Requisição de registro
     * @throws FormatoInvalidoException Se algum campo for inválido
     */
    @Override
    public void validar(RegisterRequest request) {
        // Valida username
        if (request.username() == null || request.username().isBlank()) {
            throw new FormatoInvalidoException("Username é obrigatório.");
        }
        
        if (request.username().length() < 3 || request.username().length() > 50) {
            throw new FormatoInvalidoException("Username deve ter entre 3 e 50 caracteres.");
        }
        
        if (!request.username().matches("^[a-zA-Z0-9_-]+$")) {
            throw new FormatoInvalidoException("Username deve conter apenas letras, números, '_' ou '-'.");
        }
        
        // Valida password
        if (request.password() == null || request.password().isBlank()) {
            throw new FormatoInvalidoException("Password é obrigatório.");
        }
        
        if (request.password().length() < 6) {
            throw new FormatoInvalidoException("Password deve ter no mínimo 6 caracteres.");
        }
        
        // Valida email (se fornecido)
        if (request.email() != null && !request.email().isBlank()) {
            if (!EMAIL_PATTERN.matcher(request.email()).matches()) {
                throw new FormatoInvalidoException("Email inválido.");
            }
        }
    }
}
