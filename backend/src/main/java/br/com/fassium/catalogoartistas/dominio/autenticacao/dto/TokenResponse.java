package br.com.fassium.catalogoartistas.dominio.autenticacao.dto;

public record TokenResponse(
        String accessToken,
        String refreshToken) {
}
