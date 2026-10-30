package br.com.fassium.catalogoartistas.common.exceptions;

public class RefreshTokenInvalidoException extends RuntimeException {
    public RefreshTokenInvalidoException() {
        super("Refresh token inválido ou expirado.");
    }
}

