package br.com.fassium.catalogoartistas.dominio.album.repository;

import br.com.fassium.catalogoartistas.dominio.album.model.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<Album, Long> {

    Page<Album> findByTituloContainingIgnoreCase(String titulo, Pageable pageable);

}
