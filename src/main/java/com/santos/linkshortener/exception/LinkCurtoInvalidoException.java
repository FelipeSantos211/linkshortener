package com.santos.linkshortener.exception;

/**
 * Exceção lançada quando um link curto é inválido ou já existe.
 * Usada para validar unicidade e formato de códigos curtos.
 */
public class LinkCurtoInvalidoException extends RuntimeException{
    /**
     * Construtor com mensagem de erro.
     * 
     * @param message Descrição do problema com o link curto
     */
    public LinkCurtoInvalidoException(String message) {
        super(message);
    }
}
