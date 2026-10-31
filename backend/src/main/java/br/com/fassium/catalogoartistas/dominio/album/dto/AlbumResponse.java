package br.com.fassium.catalogoartistas.dominio.album.dto;

import br.com.fassium.catalogoartistas.dominio.album.model.Album;
import br.com.fassium.catalogoartistas.dominio.artista.dto.ArtistaResumoResponse;

import java.util.List;

public record AlbumResponse(
        Long id,
        String titulo,
        Integer anoLancamento,
        List<ArtistaResumoResponse> artistas
) {
    public static AlbumResponse from(Album album) {
        
        List<ArtistaResumoResponse> artistas = album.getArtistas()
                .stream()
                .map(ArtistaResumoResponse::from)
                .toList();

        return new AlbumResponse(
                album.getId(),
                album.getTitulo(),
                album.getAnoLancamento(),
                artistas);
    }
}
