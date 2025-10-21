package com.santos.linkshortener.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entidade JPA representando um link encurtado.
 * Armazena a URL original, código curto, estatísticas e relacionamento com usuário.
 */
@Entity
@Table(name = "links")
@Getter
@Setter
@NoArgsConstructor
public class Link {
    /** Identificador único do link */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** URL original completa a ser redirecionada */
    @Column(nullable = false)
    private String urlOriginal;

    /** Código curto único para acesso ao link */
    @Column(nullable = false, unique = true)
    private String urlCurta;

    /** Contador de cliques/acessos ao link */
    private Integer contadorCliques = 0;

    /** Data e hora de criação do link */
    private LocalDateTime dataCriacao = LocalDateTime.now();

    /** Usuário proprietário do link */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Construtor para criação de link com URL original e código curto.
     * 
     * @param urlOriginal URL completa a ser encurtada
     * @param urlCurta    Código curto para o link
     */
    public Link(String urlOriginal, String urlCurta) {
        this.urlOriginal = urlOriginal;
        this.urlCurta = urlCurta;
    }
}
