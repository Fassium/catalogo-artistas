package br.com.fassium.catalogoartistas.dominio.autenticacao;

import br.com.fassium.catalogoartistas.dominio.autenticacao.model.RefreshToken;
import br.com.fassium.catalogoartistas.dominio.autenticacao.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByHash(String hash);

    @Modifying
    @Query("update RefreshToken r set r.revogado = true where r.usuario = :usuario and r.revogado = false")
    void revogarTodosDoUsuario(@Param("usuario") Usuario usuario);


}
