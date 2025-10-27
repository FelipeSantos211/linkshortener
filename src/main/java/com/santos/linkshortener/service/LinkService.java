package com.santos.linkshortener.service;

import com.santos.linkshortener.util.LinkGeneratorUtil;
import com.santos.linkshortener.dto.LinkCreateRequest;
import com.santos.linkshortener.model.Link;
import com.santos.linkshortener.model.User;
import com.santos.linkshortener.repository.LinkRepository;
import com.santos.linkshortener.repository.UserRepository;
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
    private UserRepository userRepository;

    @Autowired
    private List<Validar<LinkCreateRequest>> validadores;

    /**
     * Cria um link curto associado a um usuário autenticado.
     *
     * @param request  Dados do link a ser criado
     * @param username Username do usuário autenticado
     */
    public void createShortLink(LinkCreateRequest request, String username) {
        validadores.forEach(validador -> validador.validar(request));
        
        // Buscar o usuário pelo username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + username));
        
        String urlCurta = request.urlCurta();
        if (urlCurta == null || urlCurta.isBlank()) {
            urlCurta = LinkGeneratorUtil.gerarLinkAleatorio();
        }
        
        Link link = new Link(request.urlOriginal(), urlCurta);
        link.setUser(user); // Vincula o link ao usuário
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
    
    /**
     * Deleta um link específico associado a um usuário autenticado.
     * @param id       ID do link a ser deletado
     * @param username Username do usuário autenticado
     */
        public void deleteLink(Long id, String username) {
        Link link = linkRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Link não encontrado com id: " + id));
        if (!link.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Usuário nao autorizado para deletar o link");
        }
        linkRepository.delete(link);
    }

    /**
     * Retorna todos os links criados por um usuário específico.
     *
     * @param username Username do usuário
     * @return Lista de links do usuário
     */
    public List<Link> getLinksByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + username));
        return linkRepository.findByUser(user);
    }

}
