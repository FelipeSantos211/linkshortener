package com.santos.linkshortener.exception;

/**
 * Exceção lançada quando dados fornecidos têm formato inválido.
 * Usada em validações de entrada de dados.
 */
public class FormatoInvalidoException extends RuntimeException{
    /**
     * Construtor com mensagem de erro.
     * 
     * @param message Descrição do erro de formato
     */
    public FormatoInvalidoException(String message) {
        super(message);
    }
}
