package br.com.fassium.catalogoartistas.dominio.album;

import br.com.fassium.catalogoartistas.dominio.album.dto.AlbumResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
