package br.com.fassium.catalogoartistas.dominio.autenticacao;

import br.com.fassium.catalogoartistas.dominio.autenticacao.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByHash(String hash);

}
