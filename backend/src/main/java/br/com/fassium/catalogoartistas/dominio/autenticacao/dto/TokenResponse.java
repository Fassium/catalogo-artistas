package br.com.fassium.catalogoartistas.dominio.autenticacao.dto;

public record TokenResponse(
        String accessToken,
        String refreshToken,
        String tipo,
        long expiraEmSegundos) {

    private static final String TIPO_BEARER = "Bearer";

    public static TokenResponse bearer(String accessToken, String refreshToken, long expiraEmSegundos) {
        return new TokenResponse(accessToken, refreshToken, TIPO_BEARER, expiraEmSegundos);
    }
}
