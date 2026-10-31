package br.com.fassium.catalogoartistas.dominio.artista.dto;

import br.com.fassium.catalogoartistas.dominio.artista.model.Artista;
import br.com.fassium.catalogoartistas.dominio.artista.model.TipoArtista;

public record ArtistaResumoResponse(
        Long id,
        String nome,
        TipoArtista tipo
) {
    public static ArtistaResumoResponse from(Artista artista) {
        return new ArtistaResumoResponse(
                artista.getId(),
                artista.getNome(),
                artista.getTipo());
    }
}
