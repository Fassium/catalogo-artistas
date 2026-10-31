package br.com.fassium.catalogoartistas.dominio.album;

import br.com.fassium.catalogoartistas.dominio.album.dto.AlbumFiltro;
import br.com.fassium.catalogoartistas.dominio.album.model.Album;
import br.com.fassium.catalogoartistas.dominio.artista.model.TipoArtista;
import org.springframework.data.jpa.domain.Specification;

public final class AlbumSpecification {

    private AlbumSpecification() {
    }

    public static Specification<Album> tituloContem(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            return Specification.unrestricted();
        }

        return (root, query, cb) ->
                cb.like(cb.lower(root.get("titulo")), "%" + titulo.toLowerCase() + "%");
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
