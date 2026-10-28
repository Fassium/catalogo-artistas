package br.com.fassium.catalogoartistas.dominio.album;

import br.com.fassium.catalogoartistas.dominio.album.dto.AlbumRequest;
import br.com.fassium.catalogoartistas.dominio.album.dto.AlbumResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/v1/albuns")
public class AlbumController {

    private final AlbumService albumService;

    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @GetMapping
    public Page<AlbumResponse> listar(
            @RequestParam(defaultValue = "") String titulo,
            @PageableDefault(size = 10, sort = "titulo") Pageable pageable) {
        return albumService.listar(titulo, pageable);

    }

    @GetMapping("/{id}")
    public AlbumResponse buscarPorId(@PathVariable Long id) {
        return albumService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<AlbumResponse> criar(@Valid @RequestBody AlbumRequest request) {
        AlbumResponse albumCriado = albumService.criar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(albumCriado.id())
                .toUri();
        return ResponseEntity.created(location).body(albumCriado);
    }

    @PutMapping("/{id}")
    public AlbumResponse atualizar(@PathVariable Long id, @Valid @RequestBody AlbumRequest request) {
        return albumService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        albumService.excluir(id);
        return ResponseEntity.noContent().build();
    }

}
