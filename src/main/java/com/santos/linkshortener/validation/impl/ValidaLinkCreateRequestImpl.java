package com.santos.linkshortener.validation.impl;

import com.santos.linkshortener.dto.LinkCreateRequest;
import com.santos.linkshortener.exception.FormatoInvalidoException;
import com.santos.linkshortener.validation.Validar;
import org.springframework.stereotype.Component;

/**
 * Validador para requisições de criação de link.
 * Verifica formato da URL original e caracteres permitidos no código curto.
 */
@Component
public class ValidaLinkCreateRequestImpl implements Validar<LinkCreateRequest> {

    /**
     * Valida dados da requisição de criação de link.
     * 
     * @param request Requisição contendo URL original e código curto opcional
     * @throws FormatoInvalidoException Se a URL ou código curto forem inválidos
     */
    @Override
    public void validar(LinkCreateRequest request) {
        if (request.urlOriginal() == null || !request.urlOriginal().startsWith("http")) {
            throw new FormatoInvalidoException("URL original inválida.");
        }

        if (request.urlCurta() != null && !request.urlCurta().matches("^[a-zA-Z0-9_-]+$")) {
            throw new FormatoInvalidoException("Link customizado deve conter apenas letras, números, '_' ou '-'.");
        }
    }
}
