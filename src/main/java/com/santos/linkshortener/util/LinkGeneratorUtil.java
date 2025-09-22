package com.santos.linkshortener.util;

import java.security.SecureRandom;

public class LinkGeneratorUtil {
    private static final String ALFABETO = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int TAMANHO_LINK = 8;
    private static final SecureRandom random = new SecureRandom();

    public static String gerarLinkAleatorio() {
        StringBuilder sb = new StringBuilder(TAMANHO_LINK);
        for (int i = 0; i < TAMANHO_LINK; i++) {
            sb.append(ALFABETO.charAt(random.nextInt(ALFABETO.length())));
        }
        return sb.toString();
    }
}
