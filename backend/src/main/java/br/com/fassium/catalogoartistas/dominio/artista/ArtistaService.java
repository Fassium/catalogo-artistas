package br.com.fassium.catalogoartistas.dominio.artista;

import br.com.fassium.catalogoartistas.common.exceptions.RecursoNaoEncontradoException;
import br.com.fassium.catalogoartistas.dominio.artista.dto.ArtistaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ArtistaService {

    private final ArtistaRepository artistaRepository;

    public ArtistaService(ArtistaRepository artistaRepository) {
        this.artistaRepository = artistaRepository;
    }

    @Transactional(readOnly = true)
    public Page<ArtistaResponse> listar(String nome, Pageable pageable) {
        return artistaRepository.findByNomeContainingIgnoreCase(nome, pageable)
                .map(ArtistaResponse::from);
    }

    @Transactional(readOnly = true)
    public ArtistaResponse buscarPorId(Long id) {
        return artistaRepository.findById(id)
                .map(ArtistaResponse::from)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Artista não encontrado: " + id));
    }

}
