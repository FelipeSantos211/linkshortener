package com.santos.linkshortener.repository;

import com.santos.linkshortener.model.Link;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkRepository extends JpaRepository<Link, Long> {
    Link findByUrlCurta(String urlCurta);
}
