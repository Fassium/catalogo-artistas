package br.com.fassium.catalogoartistas.dominio.autenticacao;

import br.com.fassium.catalogoartistas.dominio.autenticacao.model.RefreshToken;
import br.com.fassium.catalogoartistas.dominio.autenticacao.model.Usuario;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    /**
     * SELECT ... FOR UPDATE: trava a linha até o fim da transação. Dois refresh simultâneos com o mesmo token
     * passam um de cada vez; o segundo já encontra o token revogado (sem o lock, os dois geravam pares válidos).
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<RefreshToken> findByHash(String hash);

    @Modifying
    @Query("update RefreshToken r set r.revogado = true where r.usuario = :usuario and r.revogado = false")
    void revogarTodosDoUsuario(@Param("usuario") Usuario usuario);

}
