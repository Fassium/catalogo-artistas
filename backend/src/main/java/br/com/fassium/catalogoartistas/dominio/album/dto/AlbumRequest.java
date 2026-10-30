package br.com.fassium.catalogoartistas.dominio.album.dto;

import br.com.fassium.catalogoartistas.dominio.album.model.Album;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AlbumRequest(

        @NotBlank
        @Size(max = 200)
        String titulo,

        Integer anoLancamento

) {
    /** Remove espaços das pontas: "  Mais do Mesmo  " vira "Mais do Mesmo", e "   " vira "" e é barrado pelo @NotBlank. */
    public AlbumRequest {
        titulo = titulo == null ? null : titulo.trim();
    }

    public Album toEntity() {
        return new Album(titulo, anoLancamento);
    }
}
