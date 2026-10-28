package br.com.fassium.catalogoartistas.dominio.artista;

import br.com.fassium.catalogoartistas.dominio.artista.dto.ArtistaRequest;
import br.com.fassium.catalogoartistas.dominio.artista.dto.ArtistaResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

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

    @GetMapping("/{id}")
    public ArtistaResponse buscarPorId(@PathVariable Long id) {
        return artistaService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<ArtistaResponse> criar(@Valid @RequestBody ArtistaRequest request) {
        ArtistaResponse artistaCriado = artistaService.criar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(artistaCriado.id())
                .toUri();
        return ResponseEntity.created(location).body(artistaCriado);
    }

    @PutMapping("/{id}")
    public ArtistaResponse atualizar(@PathVariable Long id, @Valid @RequestBody ArtistaRequest request) {
        return artistaService.atualizar(id, request);
    }


}
