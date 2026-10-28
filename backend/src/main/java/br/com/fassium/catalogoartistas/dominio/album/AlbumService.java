package br.com.fassium.catalogoartistas.dominio.album;

import br.com.fassium.catalogoartistas.common.exceptions.RecursoNaoEncontradoException;
import br.com.fassium.catalogoartistas.dominio.album.dto.AlbumRequest;
import br.com.fassium.catalogoartistas.dominio.album.dto.AlbumResponse;
import br.com.fassium.catalogoartistas.dominio.album.model.Album;
import br.com.fassium.catalogoartistas.dominio.artista.ArtistaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final ArtistaRepository artistaRepository;

    public AlbumService(AlbumRepository albumRepository, ArtistaRepository artistaRepository) {
        this.albumRepository = albumRepository;
        this.artistaRepository = artistaRepository;
    }

    @Transactional(readOnly = true)
    public Page<AlbumResponse> listar(String titulo, Pageable pageable) {
        return albumRepository.findByTituloContainingIgnoreCase(titulo, pageable)
                .map(AlbumResponse::from);
    }

    @Transactional(readOnly = true)
    public AlbumResponse buscarPorId(Long id) {
        return AlbumResponse.from(buscarAlbumPorId(id));
    }

    @Transactional
    public AlbumResponse criar(AlbumRequest request) {
        Album albumCriado = albumRepository.save(request.toEntity());
        return AlbumResponse.from(albumCriado);
    }

    @Transactional
    public AlbumResponse atualizar(Long id, AlbumRequest request) {

        Album album = buscarAlbumPorId(id);

        album.setTitulo(request.titulo());
        album.setAnoLancamento(request.anoLancamento());

        return AlbumResponse.from(album);
    }

    @Transactional
    public void excluir(Long id) {
        Album album = buscarAlbumPorId(id);

        artistaRepository.findByAlbunsId(id)
                .forEach(artista -> artista.desvincularAlbum(album));

        albumRepository.delete(album);
    }

    private Album buscarAlbumPorId(Long albumId) {
        return albumRepository.findById(albumId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Álbum não encontrado: " + albumId));
    }

}
