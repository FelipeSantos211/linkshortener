package com.santos.linkshortener.controller;

import com.santos.linkshortener.dto.LinkCreateRequest;
import com.santos.linkshortener.dto.LinkResponse;
import com.santos.linkshortener.service.LinkService;
import com.santos.linkshortener.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class LinkController {

    @Autowired
    private LinkService linkService;

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${app.base.url}")
    private String baseUrl;

    @PostMapping("/links")
    @ResponseStatus(HttpStatus.CREATED)
    public void createShortLink(
            @RequestBody LinkCreateRequest request,
            @RequestHeader("Authorization") String authHeader) {
        String username = extractUsernameFromToken(authHeader);
        linkService.createShortLink(request, username);
    }

    @GetMapping("/{shortUrl}")
    public void redirectToOriginalUrl(@PathVariable String shortUrl, HttpServletResponse response) throws IOException {
        String originalUrl = linkService.getOriginalUrl(shortUrl);
        response.sendRedirect(originalUrl);
    }

    @GetMapping("/my-links")
    public List<LinkResponse> getMyLinks(@RequestHeader("Authorization") String authHeader) {
        String username = extractUsernameFromToken(authHeader);
        return linkService.getLinksByUsername(username).stream()
                .map(link -> LinkResponse.from(link, baseUrl))
                .toList();
    }

    @DeleteMapping("/links/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLink(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {
        String username = extractUsernameFromToken(authHeader);
        linkService.deleteLink(id, username);
    }

    /**
     * Extrai o username do token JWT do header Authorization.
     * Espera o formato: "Bearer {token}"
     */
    private String extractUsernameFromToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Token JWT inválido ou ausente");
        }
        String token = authHeader.substring(7);
        return jwtUtil.extractUsername(token)
                .orElseThrow(() -> new IllegalArgumentException("Token JWT inválido"));
    }
}
