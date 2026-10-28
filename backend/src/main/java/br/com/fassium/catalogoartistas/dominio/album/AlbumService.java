package br.com.fassium.catalogoartistas.dominio.album;

import br.com.fassium.catalogoartistas.common.exceptions.RecursoNaoEncontradoException;
import br.com.fassium.catalogoartistas.dominio.album.dto.AlbumRequest;
import br.com.fassium.catalogoartistas.dominio.album.dto.AlbumResponse;
import br.com.fassium.catalogoartistas.dominio.album.model.Album;
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

    @Transactional(readOnly = true)
    public AlbumResponse buscarPorId(Long id) {
        return albumRepository.findById(id)
                .map(AlbumResponse::from)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Álbum não encontrado: " + id));
    }

    @Transactional
    public AlbumResponse criar(AlbumRequest request) {
        Album albumCriado = albumRepository.save(request.toEntity());
        return AlbumResponse.from(albumCriado);
    }

    @Transactional
    public AlbumResponse atualizar(Long id, AlbumRequest request) {

        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Álbum não encontrado: " + id));

        album.setTitulo(request.titulo());
        album.setAnoLancamento(request.anoLancamento());

        return AlbumResponse.from(album);

    }

}
