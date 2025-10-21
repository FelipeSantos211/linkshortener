package com.santos.linkshortener.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

/**
 * Utilitário moderno para geração e validação de tokens JWT.
 * Utiliza as APIs mais recentes do JJWT e práticas recomendadas de segurança.
 */
@Component
public class JwtUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    
    private final SecretKey key;
    private final Duration expirationTime;
    
    /**
     * Construtor com injeção de dependências para configurações externalizadas.
     * 
     * @param secretKey chave secreta configurada no application.properties
     * @param expirationHours tempo de expiração em horas
     */
    public JwtUtil(
            @Value("${jwt.secret:SUA_CHAVE_SECRETA_MUITO_GRANDE_E_SEGURA_AQUI_123456}") String secretKey,
            @Value("${jwt.expiration.hours:1}") long expirationHours) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.expirationTime = Duration.ofHours(expirationHours);
    }
    
    /**
     * Gera um token JWT para o usuário especificado.
     * 
     * @param username nome do usuário
     * @return token JWT gerado
     */
    public String generateToken(String username) {
        var now = Instant.now();
        var expiration = now.plus(expirationTime);
        
        return Jwts.builder()
                .subject(username)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(key)
                .compact();
    }
    
    /**
     * Extrai o nome de usuário do token JWT.
     * 
     * @param token token JWT
     * @return Optional contendo o username ou vazio se inválido
     */
    public Optional<String> extractUsername(String token) {
        return extractClaims(token)
                .map(Claims::getSubject);
    }
    
    /**
     * Extrai as claims do token JWT.
     * 
     * @param token token JWT
     * @return Optional contendo as claims ou vazio se inválido
     */
    public Optional<Claims> extractClaims(String token) {
        try {
            var claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return Optional.of(claims);
        } catch (JwtException e) {
            logger.warn("Falha ao extrair claims do token: {}", e.getMessage());
            return Optional.empty();
        }
    }
    
    /**
     * Valida se o token JWT é válido.
     * 
     * @param token token JWT a ser validado
     * @return true se o token é válido, false caso contrário
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            logger.debug("Token inválido: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Verifica se o token está expirado.
     * 
     * @param token token JWT
     * @return true se o token está expirado, false caso contrário
     */
    public boolean isTokenExpired(String token) {
        return extractClaims(token)
                .map(Claims::getExpiration)
                .map(expiration -> expiration.before(new Date()))
                .orElse(true);
    }
}
