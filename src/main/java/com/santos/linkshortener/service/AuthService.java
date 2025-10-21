package com.santos.linkshortener.service;

import com.santos.linkshortener.dto.AuthResponse;
import com.santos.linkshortener.dto.LoginRequest;
import com.santos.linkshortener.dto.RegisterRequest;
import com.santos.linkshortener.model.User;
import com.santos.linkshortener.repository.UserRepository;
import com.santos.linkshortener.util.JwtUtil;
import com.santos.linkshortener.validation.Validar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Serviço responsável pela autenticação e registro de usuários.
 */
@Service
public class AuthService {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final Validar<LoginRequest> validaLoginRequest;
    private final Validar<RegisterRequest> validaRegisterRequest;
    private final Validar<RegisterRequest> validaUsernameUnico;
    
    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil,
            @Qualifier("validaLoginRequestImpl") Validar<LoginRequest> validaLoginRequest,
            @Qualifier("validaRegisterRequestImpl") Validar<RegisterRequest> validaRegisterRequest,
            @Qualifier("validaUsernameUnicoImpl") Validar<RegisterRequest> validaUsernameUnico) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.validaLoginRequest = validaLoginRequest;
        this.validaRegisterRequest = validaRegisterRequest;
        this.validaUsernameUnico = validaUsernameUnico;
    }
    
    /**
     * Autentica um usuário e retorna um token JWT.
     *
     * @param request dados de login
     * @return resposta contendo o token JWT
     * @throws BadCredentialsException se as credenciais forem inválidas
     */
    public AuthResponse login(LoginRequest request) {
        // Valida o request
        validaLoginRequest.validar(request);
        
        // Busca o usuário no banco
        var user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> {
                    logger.warn("Usuário não encontrado: {}", request.username());
                    return new BadCredentialsException("Credenciais inválidas");
                });
        
        // Verifica a senha
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            logger.warn("Senha incorreta para usuário: {}", request.username());
            throw new BadCredentialsException("Credenciais inválidas");
        }
        
        // Gera o token JWT
        var token = jwtUtil.generateToken(request.username());
        
        logger.info("Usuário autenticado com sucesso: {}", request.username());
        return AuthResponse.success(token, request.username());
    }
    
    /**
     * Registra um novo usuário no sistema.
     *
     * @param request dados de registro
     * @return resposta contendo o token JWT do novo usuário
     * @throws IllegalArgumentException se o usuário já existir
     */
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Valida o request
        validaRegisterRequest.validar(request);
        
        // Valida se o username é único
        validaUsernameUnico.validar(request);
        
        // Cria novo usuário
        var user = new User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setEmail(request.email());
        
        userRepository.save(user);
        
        // Gera token para o novo usuário
        var token = jwtUtil.generateToken(request.username());
        
        logger.info("Novo usuário registrado: {}", request.username());
        return AuthResponse.registered(token, request.username());
    }
    
    /**
     * Valida se um token JWT é válido.
     *
     * @param token token JWT a ser validado
     * @return true se válido, false caso contrário
     */
    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token);
    }
    
    /**
     * Extrai o username de um token JWT.
     *
     * @param token token JWT
     * @return username ou null se inválido
     */
    public String extractUsername(String token) {
        return jwtUtil.extractUsername(token).orElse(null);
    }
}
