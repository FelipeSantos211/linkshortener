package com.santos.linkshortener.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.santos.linkshortener.repository.UserRepository;
import com.santos.linkshortener.util.JwtUtil;

import java.io.IOException;
import java.util.Optional;

/**
 * Filtro de segurança para validação de tokens JWT em requisições.
 * Intercepta requisições HTTP e autentica usuários baseado em tokens válidos.
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    /**
     * Construtor com injeção de dependências.
     * 
     * @param jwtUtil          Utilitário para operações com JWT
     * @param userRepository   Repositório para buscar usuários
     */
    public JwtFilter(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    /**
     * Processa cada requisição HTTP para validar e autenticar via JWT.
     * Extrai o token do header Authorization, valida e configura o contexto de segurança.
     * 
     * @param request     Requisição HTTP
     * @param response    Resposta HTTP
     * @param filterChain Cadeia de filtros
     * @throws ServletException Se houver erro no processamento
     * @throws IOException      Se houver erro de I/O
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (jwtUtil.validateToken(token)) {
                Optional<String> username = jwtUtil.extractUsername(token);
                if (username.isPresent()) {
                    var user = userRepository.findByUsername(username.get());
                    if (user != null) {
                        var authentication = new UsernamePasswordAuthenticationToken(
                                username.get(), null, null);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
