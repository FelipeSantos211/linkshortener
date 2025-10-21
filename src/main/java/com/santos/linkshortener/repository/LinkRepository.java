package com.santos.linkshortener.repository;

import com.santos.linkshortener.model.Link;
import com.santos.linkshortener.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositório JPA para gerenciamento de links encurtados.
 * Fornece operações de CRUD e consultas personalizadas.
 */
public interface LinkRepository extends JpaRepository<Link, Long> {
    /**
     * Busca um link pelo código curto.
     * 
     * @param urlCurta Código curto do link
     * @return Link encontrado ou null
     */
    Link findByUrlCurta(String urlCurta);
    
    /**
     * Busca todos os links de um usuário específico.
     * 
     * @param user Usuário proprietário dos links
     * @return Lista de links do usuário
     */
    List<Link> findByUser(User user);
}
