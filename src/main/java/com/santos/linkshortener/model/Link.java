package com.santos.linkshortener.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "links")
@Getter
@Setter
@NoArgsConstructor
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String urlOriginal;

    @Column(nullable = false, unique = true)
    private String urlCurta;

    private Integer contadorCliques = 0;

    private LocalDateTime dataCriacao = LocalDateTime.now();

    public Link(String urlOriginal, String urlCurta) {
        this.urlOriginal = urlOriginal;
        this.urlCurta = urlCurta;
    }
}
