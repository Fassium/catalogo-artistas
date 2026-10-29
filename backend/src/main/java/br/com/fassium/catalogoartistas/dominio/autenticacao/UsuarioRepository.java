package br.com.fassium.catalogoartistas.dominio.autenticacao;

import br.com.fassium.catalogoartistas.dominio.autenticacao.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

}
