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
        public Album toEntity(){
                return new Album(titulo, anoLancamento);
        }
}
