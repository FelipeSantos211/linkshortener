package com.santos.linkshortener.dto;

/**
 * DTO para requisição de criação de link curto.
 * 
 * @param urlOriginal URL original completa a ser encurtada
 * @param urlCurta    Código curto personalizado (opcional). Se não fornecido, será gerado automaticamente
 */
public record LinkCreateRequest(
        String urlOriginal,
        String urlCurta) {
}
