package com.santos.linkshortener.dto;

/**
 * DTO para resposta de autenticação contendo o token JWT.
 * 
 * @param token    Token JWT para autenticação em requisições futuras
 * @param username Nome de usuário autenticado
 * @param message  Mensagem de status da operação
 */
public record AuthResponse(
        String token,
        String username,
        String message
) {
    /**
     * Cria uma resposta de sucesso para login.
     * 
     * @param token    Token JWT gerado
     * @param username Nome do usuário autenticado
     * @return Resposta formatada de sucesso
     */
    public static AuthResponse success(String token, String username) {
        return new AuthResponse(token, username, "Autenticação realizada com sucesso");
    }
    
    /**
     * Cria uma resposta de sucesso para registro de usuário.
     * 
     * @param token    Token JWT gerado
     * @param username Nome do usuário registrado
     * @return Resposta formatada de registro
     */
    public static AuthResponse registered(String token, String username) {
        return new AuthResponse(token, username, "Usuário registrado com sucesso");
    }
}
