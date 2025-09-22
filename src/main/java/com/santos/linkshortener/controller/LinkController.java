package com.santos.linkshortener.controller;

import com.santos.linkshortener.dto.LinkCreateRequest;
import com.santos.linkshortener.model.Link;
import com.santos.linkshortener.service.LinkService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1/links")
public class LinkController {

    @Autowired
    private LinkService linkService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createShortLink(@RequestBody LinkCreateRequest request) {
        linkService.createShortLink(request);
    }

    @GetMapping("/{shortUrl}")
    public void redirectToOriginalUrl(@PathVariable String shortUrl, HttpServletResponse response) throws IOException {
        String originalUrl = linkService.getOriginalUrl(shortUrl);
        response.sendRedirect(originalUrl);
    }

    @GetMapping
    public List<Link> getAllLinks() {
        return linkService.getAllLinks();
    }


}
