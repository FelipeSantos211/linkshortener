package com.santos.linkshortener.dto;

import com.santos.linkshortener.model.Link;

import java.time.LocalDateTime;

/**
 * DTO para resposta de links, evitando serialização circular.
 */
public record LinkResponse(
        Long id,
        String urlOriginal,
        String urlCurta, // URL completa: http://localhost:8080/api/v1/links/{shortCode}
        Integer contadorCliques,
        LocalDateTime dataCriacao,
        String username) {

    /**
     * Converte uma entidade Link para LinkResponse com URL completa.
     * 
     * @param link    Entidade Link
     * @param baseUrl URL base configurada (ex: http://localhost:8080/api/v1/links)
     */
    public static LinkResponse from(Link link, String baseUrl) {
        String urlCompletaCurta = baseUrl + "/" + link.getUrlCurta();
        return new LinkResponse(
                link.getId(),
                link.getUrlOriginal(),
                urlCompletaCurta,
                link.getContadorCliques(),
                link.getDataCriacao(),
                link.getUser() != null ? link.getUser().getUsername() : null
        );
    }
}
