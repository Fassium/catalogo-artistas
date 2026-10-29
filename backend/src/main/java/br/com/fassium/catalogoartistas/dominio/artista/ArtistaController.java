package br.com.fassium.catalogoartistas.dominio.artista;

import br.com.fassium.catalogoartistas.dominio.artista.dto.ArtistaRequest;
import br.com.fassium.catalogoartistas.dominio.artista.dto.ArtistaResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
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

    @Operation(summary = "Lista artistas com paginação e busca por nome")
    @ApiResponse(responseCode = "200", description = "Página de artistas")
    @GetMapping
    public Page<ArtistaResponse> listar(
            @RequestParam(defaultValue = "") String nome,
            @PageableDefault(size = 10, sort = "nome") @ParameterObject Pageable pageable) {
        return artistaService.listar(nome, pageable);

    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar um artista pelo id")
    @ApiResponse(responseCode = "200", description = "Artista encontrado")
    @ApiResponse(responseCode = "404", description = "Artista não encontrado", content = @Content)
    public ArtistaResponse buscarPorId(@PathVariable Long id) {
        return artistaService.buscarPorId(id);
    }

    @Operation(summary = "Cria um artista")
    @ApiResponse(responseCode = "201", description = "Artista criado")
    @ApiResponse(responseCode = "422", description = "Dados inválidos", content = @Content)
    @PostMapping
    public ResponseEntity<ArtistaResponse> criar(@Valid @RequestBody ArtistaRequest request) {
        ArtistaResponse artistaCriado = artistaService.criar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(artistaCriado.id())
                .toUri();
        return ResponseEntity.created(location).body(artistaCriado);
    }

    @Operation(summary = "Atualiza um artista")
    @ApiResponse(responseCode = "200", description = "Artista atualizado")
    @ApiResponse(responseCode = "404", description = "Artista não encontrado", content = @Content)
    @ApiResponse(responseCode = "422", description = "Dados inválidos", content = @Content)
    @PutMapping("/{id}")
    public ArtistaResponse atualizar(@PathVariable Long id, @Valid @RequestBody ArtistaRequest request) {
        return artistaService.atualizar(id, request);
    }

    @Operation(summary = "Vincula um álbum a um artista")
    @ApiResponse(responseCode = "204", description = "Álbum vinculado")
    @ApiResponse(responseCode = "404", description = "Artista ou álbum não encontrado", content = @Content)
    @PostMapping("/{artistaId}/albuns/{albumId}")
    public ResponseEntity<Void> vincularAlbum(@PathVariable Long artistaId, @PathVariable Long albumId) {
        artistaService.vincularAlbum(artistaId, albumId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Desvincula um álbum de um artista")
    @ApiResponse(responseCode = "204", description = "Álbum desvinculado")
    @ApiResponse(responseCode = "404", description = "Artista ou álbum não encontrado", content = @Content)
    @DeleteMapping("/{artistaId}/albuns/{albumId}")
    public ResponseEntity<Void> desvincularAlbum(@PathVariable Long artistaId, @PathVariable Long albumId) {
        artistaService.desvincularAlbum(artistaId, albumId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Exclui um artista")
    @ApiResponse(responseCode = "204", description = "Artista excluído")
    @ApiResponse(responseCode = "404", description = "Artista não encontrado", content = @Content)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        artistaService.excluir(id);
        return ResponseEntity.noContent().build();
    }

}
