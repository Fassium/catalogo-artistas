package br.com.fassium.catalogoartistas.dominio.album.dto;

import br.com.fassium.catalogoartistas.dominio.artista.model.TipoArtista;

public record AlbumFiltro(
        String titulo,
        TipoArtista tipoArtista,
        Long artistaId) {
}
