package com.santos.linkshortener.validation.impl;

import com.santos.linkshortener.dto.LinkCreateRequest;
import com.santos.linkshortener.exception.FormatoInvalidoException;
import com.santos.linkshortener.validation.Validar;
import org.springframework.stereotype.Component;

@Component
public class ValidaLinkCreateRequestImpl implements Validar<LinkCreateRequest> {

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
