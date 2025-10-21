package com.santos.linkshortener.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para requisição de registro de novo usuário.
 * 
 * @param username Nome de usuário (3-50 caracteres)
 * @param password Senha do usuário (mínimo 6 caracteres)
 * @param email    Email do usuário
 */
public record RegisterRequest(
        @NotBlank(message = "Username é obrigatório")
        @Size(min = 3, max = 50, message = "Username deve ter entre 3 e 50 caracteres")
        String username,
        
        @NotBlank(message = "Password é obrigatório")
        @Size(min = 6, message = "Password deve ter no mínimo 6 caracteres")
        String password,
        
        String email
) {}
