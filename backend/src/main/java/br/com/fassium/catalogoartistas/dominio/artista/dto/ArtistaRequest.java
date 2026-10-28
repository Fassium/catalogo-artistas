package br.com.fassium.catalogoartistas.dominio.artista.dto;

import br.com.fassium.catalogoartistas.dominio.artista.model.Artista;
import br.com.fassium.catalogoartistas.dominio.artista.model.TipoArtista;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ArtistaRequest(

        @NotBlank
        @Size(max = 200)
        String nome,

        @NotNull
        TipoArtista tipo
) {
    public Artista toEntity() {
        return new Artista(nome, tipo);
    }
}
