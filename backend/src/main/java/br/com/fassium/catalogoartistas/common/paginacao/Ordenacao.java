package br.com.fassium.catalogoartistas.common.paginacao;

import br.com.fassium.catalogoartistas.common.exceptions.OrdenacaoInvalidaException;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Confere o "sort" pedido pelo cliente contra uma lista de campos permitidos.
 * Sem isso, ?sort=albuns.titulo faz o Hibernate juntar a coleção: o LIMIT/OFFSET passa a contar linhas do join
 * (registros repetidos ou pulados entre páginas) e, com DISTINCT, o PostgreSQL recusa a consulta (500).
 */
public final class Ordenacao {

    private Ordenacao() {
    }

    public static void validar(Pageable pageable, List<String> camposPermitidos) {
        pageable.getSort().forEach(ordem -> {
            if (!camposPermitidos.contains(ordem.getProperty())) {
                throw new OrdenacaoInvalidaException(ordem.getProperty(), camposPermitidos);
            }
        });
    }
}
