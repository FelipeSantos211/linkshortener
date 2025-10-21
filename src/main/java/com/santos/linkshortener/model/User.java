package com.santos.linkshortener.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Entidade JPA representando um usuário do sistema.
 * Gerencia autenticação e propriedade de links encurtados.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    /** Identificador único do usuário */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nome de usuário único para login */
    @Column(nullable = false, unique = true)
    private String username;

    /** Email único do usuário */
    @Column(nullable = false, unique = true)
    private String email;
    
    /** Senha criptografada (BCrypt) */
    @Column(nullable = false)
    private String password;

    /** Lista de links criados pelo usuário */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore // Evita serialização circular
    private List<Link> links;
}
