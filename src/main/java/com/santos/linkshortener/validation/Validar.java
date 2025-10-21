package com.santos.linkshortener.validation;

/**
 * Interface genérica para validação de objetos.
 * Implementações específicas devem lançar exceções apropriadas quando a validação falhar.
 * 
 * @param <T> Tipo do objeto a ser validado
 */
public interface Validar<T> {
    /**
     * Valida um objeto segundo regras específicas.
     * 
     * @param obj Objeto a ser validado
     * @throws RuntimeException Se a validação falhar
     */
    void validar(T obj);
}
