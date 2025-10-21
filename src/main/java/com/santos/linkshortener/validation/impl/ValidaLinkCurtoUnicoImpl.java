package com.santos.linkshortener.validation.impl;

import com.santos.linkshortener.dto.LinkCreateRequest;
import com.santos.linkshortener.exception.LinkCurtoInvalidoException;
import com.santos.linkshortener.repository.LinkRepository;
import com.santos.linkshortener.validation.Validar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Validador de unicidade de código curto.
 * Verifica se o código curto já está em uso no banco de dados.
 */
@Component
public class ValidaLinkCurtoUnicoImpl implements Validar<LinkCreateRequest> {
    @Autowired
    private LinkRepository linkRepository;

    /**
     * Valida se o código curto é único.
     * 
     * @param request Requisição contendo o código curto a validar
     * @throws LinkCurtoInvalidoException Se o código já estiver em uso
     */
    @Override
    public void validar(LinkCreateRequest request) {
        String urlCurta = request.urlCurta();
        if (urlCurta != null && !urlCurta.isBlank()) {
            if (linkRepository.findByUrlCurta(urlCurta) != null) {
                throw new LinkCurtoInvalidoException("O link curto já está em uso.");
            }
        }
    }
}
