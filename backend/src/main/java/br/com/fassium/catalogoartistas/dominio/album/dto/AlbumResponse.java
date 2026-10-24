package br.com.fassium.catalogoartistas.dominio.album.dto;

import br.com.fassium.catalogoartistas.dominio.album.model.Album;

public record AlbumResponse(
        Long id,
        String titulo,
        Integer anoLancamento
) {
    public static AlbumResponse from(Album album) {
        return new AlbumResponse(
                album.getId(),
                album.getTitulo(),
                album.getAnoLancamento());
    }
}
