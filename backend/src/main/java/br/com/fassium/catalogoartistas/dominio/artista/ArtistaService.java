package br.com.fassium.catalogoartistas.dominio.artista;

import br.com.fassium.catalogoartistas.common.exceptions.RecursoNaoEncontradoException;
import br.com.fassium.catalogoartistas.dominio.album.AlbumRepository;
import br.com.fassium.catalogoartistas.dominio.album.model.Album;
import br.com.fassium.catalogoartistas.dominio.artista.dto.ArtistaRequest;
import br.com.fassium.catalogoartistas.dominio.artista.dto.ArtistaResponse;
import br.com.fassium.catalogoartistas.dominio.artista.model.Artista;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ArtistaService {

    private final ArtistaRepository artistaRepository;
    private final AlbumRepository albumRepository;

    public ArtistaService(ArtistaRepository artistaRepository, AlbumRepository albumRepository) {
        this.artistaRepository = artistaRepository;
        this.albumRepository = albumRepository;
    }

    @Transactional(readOnly = true)
    public Page<ArtistaResponse> listar(String nome, Pageable pageable) {
        return artistaRepository.findByNomeContainingIgnoreCase(nome, pageable)
                .map(ArtistaResponse::from);
    }

    @Transactional(readOnly = true)
    public ArtistaResponse buscarPorId(Long id) {
        return ArtistaResponse.from(buscarArtistaPorId(id));
    }

    @Transactional
    public ArtistaResponse criar(ArtistaRequest request) {
        Artista artista = artistaRepository.save(request.toEntity());
        return ArtistaResponse.from(artista);
    }

    @Transactional
    public ArtistaResponse atualizar(Long id, ArtistaRequest request) {
        Artista artista = buscarArtistaPorId(id);

        artista.setNome(request.nome());
        artista.setTipo(request.tipo());

        return ArtistaResponse.from(artista);
    }

    @Transactional
    public void vincularAlbum(Long artistaId, Long albumId) {

        Artista artista = buscarArtistaPorId(artistaId);

        Album album = buscarAlbumPorId(albumId);

        artista.vincularAlbum(album);
    }

    @Transactional
    public void desvincularAlbum(Long artistaId, Long albumId) {

        Artista artista = buscarArtistaPorId(artistaId);

        Album album = buscarAlbumPorId(albumId);

        artista.desvincularAlbum(album);
    }

    @Transactional
    public void excluir(Long artistaId) {
        Artista artista = buscarArtistaPorId(artistaId);

        artistaRepository.delete(artista);
    }

    private Artista buscarArtistaPorId(Long artistaId) {
        return artistaRepository.findById(artistaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Artista não encontrado: " + artistaId));
    }

    private Album buscarAlbumPorId(Long albumId) {
        return albumRepository.findById(albumId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Álbum não encontrado: " + albumId));

    }


}
