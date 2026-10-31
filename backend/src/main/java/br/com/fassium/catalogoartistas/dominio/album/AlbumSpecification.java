package br.com.fassium.catalogoartistas.dominio.album;

import br.com.fassium.catalogoartistas.dominio.album.dto.AlbumFiltro;
import br.com.fassium.catalogoartistas.dominio.album.model.Album;
import br.com.fassium.catalogoartistas.dominio.artista.model.TipoArtista;
import org.springframework.data.jpa.domain.Specification;

import java.util.Locale;

public final class AlbumSpecification {

    private static final char CARACTERE_ESCAPE = '\\';

    private AlbumSpecification() {
    }

    public static Specification<Album> tituloContem(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            return Specification.unrestricted();
        }

        String padrao = "%" + escaparCuringas(titulo.toLowerCase(Locale.ROOT)) + "%";
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("titulo")), padrao, CARACTERE_ESCAPE);
    }

    /**
     * No LIKE, "%" e "_" são curingas: sem escapar, ?titulo=_ trazia todos os álbuns.
     * A barra (caractere de escape) é escapada primeiro para não se confundir com as outras.
     */
    private static String escaparCuringas(String texto) {
        return texto.replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");
    }

    public static Specification<Album> doTipoArtista(TipoArtista tipo) {
        if (tipo == null) {
            return Specification.unrestricted();
        }

        return (root, query, cb) -> {
            query.distinct(true);
            return cb.equal(root.join("artistas").get("tipo"), tipo);
        };
    }

    public static Specification<Album> doArtista(Long artistaId) {
        if (artistaId == null) {
            return Specification.unrestricted();
        }
        return (root, query, cb) -> {
            query.distinct(true);
            return cb.equal(root.join("artistas").get("id"), artistaId);
        };
    }

    public static Specification<Album> comFiltro(AlbumFiltro filtro) {
        return Specification.allOf(
                tituloContem(filtro.titulo()),
                doTipoArtista(filtro.tipoArtista()),
                doArtista(filtro.artistaId()));
    }

}
