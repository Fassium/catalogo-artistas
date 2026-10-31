package br.com.fassium.catalogoartistas.common.exceptions;

import java.util.List;

public class OrdenacaoInvalidaException extends RuntimeException {
    public OrdenacaoInvalidaException(String campo, List<String> camposPermitidos) {
        super("Ordenação inválida: '" + campo + "'. Campos aceitos: " + String.join(", ", camposPermitidos) + ".");
    }
}
