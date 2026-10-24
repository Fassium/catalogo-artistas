package br.com.fassium.catalogoartistas.dominio.artista;

import br.com.fassium.catalogoartistas.dominio.artista.dto.ArtistaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/artistas")
public class ArtistaController {

    private final ArtistaService artistaService;

    public ArtistaController(ArtistaService artistaService) {
        this.artistaService = artistaService;
    }

    @GetMapping
    public Page<ArtistaResponse> listar(
            @RequestParam(defaultValue = "") String nome,
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        return artistaService.listar(nome, pageable);

    }


}
