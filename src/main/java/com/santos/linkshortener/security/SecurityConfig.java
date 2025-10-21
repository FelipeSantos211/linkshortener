package com.santos.linkshortener.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuração de segurança do Spring Security.
 * Define autenticação stateless com JWT e regras de autorização.
 */
@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    /**
     * Construtor com injeção do filtro JWT.
     * 
     * @param jwtFilter Filtro para validação de tokens JWT
     */
    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    /**
     * Configura a cadeia de filtros de segurança.
     * Define endpoints públicos e autenticados, desabilita CSRF e sessões.
     * 
     * @param http Configurador de segurança HTTP
     * @return SecurityFilterChain configurada
     * @throws Exception Se houver erro na configuração
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configure(http))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll() // Autenticação pública
                .requestMatchers("/{shortUrl}").permitAll() // Redirecionamento público
                .requestMatchers("/links", "/my-links").authenticated() // CRUD de links requer autenticação
                .anyRequest().authenticated()
            )
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Bean para encoder de senhas usando BCrypt.
     * 
     * @return Encoder BCrypt para criptografia de senhas
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
