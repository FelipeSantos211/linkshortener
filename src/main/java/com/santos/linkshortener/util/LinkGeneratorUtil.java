package com.santos.linkshortener.util;

import java.security.SecureRandom;

/**
 * Utilitário para geração de códigos curtos aleatórios para links.
 * Utiliza SecureRandom para garantir aleatoriedade criptográfica.
 */
public class LinkGeneratorUtil {
    /** Alfabeto de caracteres permitidos (a-z, A-Z, 0-9) */
    private static final String ALFABETO = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    
    /** Tamanho padrão do código curto gerado */
    private static final int TAMANHO_LINK = 8;
    
    /** Gerador de números aleatórios seguro */
    private static final SecureRandom random = new SecureRandom();

    /**
     * Gera um código curto aleatório de 8 caracteres.
     * Usa caracteres alfanuméricos (a-z, A-Z, 0-9).
     * 
     * @return String com código curto aleatório
     */
    public static String gerarLinkAleatorio() {
        StringBuilder sb = new StringBuilder(TAMANHO_LINK);
        for (int i = 0; i < TAMANHO_LINK; i++) {
            sb.append(ALFABETO.charAt(random.nextInt(ALFABETO.length())));
        }
        return sb.toString();
    }
}
