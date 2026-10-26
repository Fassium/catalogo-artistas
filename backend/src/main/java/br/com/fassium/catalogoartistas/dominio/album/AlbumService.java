package br.com.fassium.catalogoartistas.dominio.album;

import br.com.fassium.catalogoartistas.dominio.album.dto.AlbumResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AlbumService {

    private final AlbumRepository albumRepository;

    public AlbumService(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    @Transactional(readOnly = true)
    public Page<AlbumResponse> listar(String titulo, Pageable pageable) {
        return albumRepository.findByTituloContainingIgnoreCase(titulo, pageable)
                .map(AlbumResponse::from);
    }
}
