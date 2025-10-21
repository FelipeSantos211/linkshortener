package com.santos.linkshortener.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO para requisição de login.
 * 
 * @param username Nome de usuário para autenticação
 * @param password Senha do usuário
 */
public record LoginRequest(
        @NotBlank(message = "Username é obrigatório")
        String username,
        
        @NotBlank(message = "Password é obrigatório")
        String password
) {}
