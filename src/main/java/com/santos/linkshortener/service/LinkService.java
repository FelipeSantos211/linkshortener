package com.santos.linkshortener.service;

import com.santos.linkshortener.util.LinkGeneratorUtil;
import com.santos.linkshortener.dto.LinkCreateRequest;
import com.santos.linkshortener.model.Link;
import com.santos.linkshortener.repository.LinkRepository;
import com.santos.linkshortener.validation.Validar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LinkService {

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private List<Validar<LinkCreateRequest>> validadores;

    public void createShortLink(LinkCreateRequest request) {
        validadores.forEach(validador -> validador.validar(request));
        String urlCurta = request.urlCurta();
        if (urlCurta == null || urlCurta.isBlank()) {
          urlCurta = LinkGeneratorUtil.gerarLinkAleatorio();
        }
        Link link = new Link(request.urlOriginal(), urlCurta);
        linkRepository.save(link);
    }

    public String getOriginalUrl(String urlCurta) {
        Optional<Link> linkOptional = Optional.ofNullable(linkRepository.findByUrlCurta(urlCurta));
        if (linkOptional.isPresent()) {
            Link link = linkOptional.get();
            link.setContadorCliques(link.getContadorCliques() + 1);
            linkRepository.save(link);
            return link.getUrlOriginal();
        } else {
            throw new RuntimeException("Link não encontrado");
        }
    }
    public List<Link> getAllLinks() {
        return linkRepository.findAll();
    }
}
