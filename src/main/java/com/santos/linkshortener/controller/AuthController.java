package com.santos.linkshortener.controller;

import com.santos.linkshortener.dto.AuthResponse;
import com.santos.linkshortener.dto.LoginRequest;
import com.santos.linkshortener.dto.RegisterRequest;
import com.santos.linkshortener.service.AuthService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsável pelos endpoints de autenticação.
 */

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/auth")
public class AuthController {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    private final AuthService authService;
    
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    
    /**
     * Endpoint para login de usuários.
     *
     * @param request dados de login
     * @return token JWT se autenticado com sucesso
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            var response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            logger.warn("Falha no login para usuário: {}", request.username());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(null, null, "Credenciais inválidas"));
        }
    }
    
    /**
     * Endpoint para registro de novos usuários.
     *
     * @param request dados de registro
     * @return token JWT do novo usuário
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        try {
            var response = authService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            logger.warn("Falha no registro: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new AuthResponse(null, null, e.getMessage()));
        }
    }
    
    /**
     * Endpoint para validar se um token é válido.
     *
     * @param token token JWT a ser validado
     * @return status da validação
     */
    @GetMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String token) {
        // Remove o prefixo "Bearer " se existir
        var jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        var isValid = authService.validateToken(jwtToken);
        return ResponseEntity.ok(isValid);
    }
    
    /**
     * Endpoint de teste para verificar se a API está funcionando.
     *
     * @return mensagem de status
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Auth Service está funcionando!");
    }
}
