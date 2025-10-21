package com.santos.linkshortener.repository;

import com.santos.linkshortener.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositório JPA para gerenciamento de usuários.
 * Fornece operações de CRUD e consultas personalizadas para autenticação.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Busca um usuário pelo nome de usuário.
     * 
     * @param username Nome de usuário a ser buscado
     * @return Optional contendo o usuário se encontrado
     */
    Optional<User> findByUsername(String username);
}

