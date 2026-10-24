package br.com.fassium.catalogoartistas.dominio.artista.dto;

import br.com.fassium.catalogoartistas.dominio.album.dto.AlbumResponse;
import br.com.fassium.catalogoartistas.dominio.artista.model.Artista;
import br.com.fassium.catalogoartistas.dominio.artista.model.TipoArtista;

import java.util.List;

public record ArtistaResponse(
        Long id,
        String nome,
        TipoArtista tipo,
        List<AlbumResponse> albuns
) {
    public static ArtistaResponse from(Artista artista) {
        List<AlbumResponse> albuns = artista.getAlbuns()
                .stream()
                .map(AlbumResponse::from)
                .toList();

        return new ArtistaResponse(
                artista.getId(),
                artista.getNome(),
                artista.getTipo(),
                albuns);
    }
}
