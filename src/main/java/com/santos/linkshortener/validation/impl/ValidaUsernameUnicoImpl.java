package com.santos.linkshortener.validation.impl;

import com.santos.linkshortener.dto.RegisterRequest;
import com.santos.linkshortener.exception.FormatoInvalidoException;
import com.santos.linkshortener.repository.UserRepository;
import com.santos.linkshortener.validation.Validar;
import org.springframework.stereotype.Component;

/**
 * Validador de unicidade de username.
 * Verifica se o nome de usuário já está registrado no sistema.
 */
@Component
public class ValidaUsernameUnicoImpl implements Validar<RegisterRequest> {
    
    private final UserRepository userRepository;
    
    /**
     * Construtor com injeção de dependência.
     * 
     * @param userRepository Repositório para consultar usuários existentes
     */
    public ValidaUsernameUnicoImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    /**
     * Valida se o username é único no sistema.
     * 
     * @param request Requisição de registro contendo o username
     * @throws FormatoInvalidoException Se o username já estiver em uso
     */
    @Override
    public void validar(RegisterRequest request) {
        if (request.username() != null && !request.username().isBlank()) {
            if (userRepository.findByUsername(request.username()).isPresent()) {
                throw new FormatoInvalidoException("Username já está em uso.");
            }
        }
    }
}
