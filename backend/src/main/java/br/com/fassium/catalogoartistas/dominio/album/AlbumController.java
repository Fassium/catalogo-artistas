package br.com.fassium.catalogoartistas.dominio.album;

import br.com.fassium.catalogoartistas.dominio.album.dto.AlbumRequest;
import br.com.fassium.catalogoartistas.dominio.album.dto.AlbumResponse;
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
@RequestMapping("/v1/albuns")
public class AlbumController {

    private final AlbumService albumService;

    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @Operation(summary = "Lista álbuns com paginação e busca por título")
    @ApiResponse(responseCode = "200", description = "Página de álbuns")
    @GetMapping
    public Page<AlbumResponse> listar(
            @RequestParam(defaultValue = "") String titulo,
            @PageableDefault(size = 10, sort = "titulo") @ParameterObject Pageable pageable) {
        return albumService.listar(titulo, pageable);

    }

    @Operation(summary = "Busca um álbum pelo id")
    @ApiResponse(responseCode = "200", description = "Álbum encontrado")
    @ApiResponse(responseCode = "404", description = "Álbum não encontrado", content = @Content)
    @GetMapping("/{id}")
    public AlbumResponse buscarPorId(@PathVariable Long id) {
        return albumService.buscarPorId(id);
    }

    @Operation(summary = "Cria um álbum")
    @ApiResponse(responseCode = "201", description = "Álbum criado")
    @ApiResponse(responseCode = "422", description = "Dados inválidos", content = @Content)
    @PostMapping
    public ResponseEntity<AlbumResponse> criar(@Valid @RequestBody AlbumRequest request) {
        AlbumResponse albumCriado = albumService.criar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(albumCriado.id())
                .toUri();
        return ResponseEntity.created(location).body(albumCriado);
    }

    @Operation(summary = "Atualiza um álbum")
    @ApiResponse(responseCode = "200", description = "Álbum atualizado")
    @ApiResponse(responseCode = "404", description = "Álbum não encontrado", content = @Content)
    @ApiResponse(responseCode = "422", description = "Dados inválidos", content = @Content)
    @PutMapping("/{id}")
    public AlbumResponse atualizar(@PathVariable Long id, @Valid @RequestBody AlbumRequest request) {
        return albumService.atualizar(id, request);
    }

    @Operation(summary = "Exclui um álbum")
    @ApiResponse(responseCode = "204", description = "Álbum excluído")
    @ApiResponse(responseCode = "404", description = "Álbum não encontrado", content = @Content)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        albumService.excluir(id);
        return ResponseEntity.noContent().build();
    }

}
