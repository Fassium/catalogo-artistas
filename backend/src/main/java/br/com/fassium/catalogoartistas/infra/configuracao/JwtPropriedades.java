package br.com.fassium.catalogoartistas.infra.configuracao;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Espelho do bloco "seguranca.jwt" do application.yaml, como objeto Java.
 * <p>
 * Fluxo: .env (JWT_CHAVE) -> application.yaml (chave: ${JWT_CHAVE}) -> esta classe -> quem precisar injeta.
 * O Spring liga "expiracao-minutos" do YAML ao campo expiracaoMinutos sozinho.
 * Registrada pelo @EnableConfigurationProperties do SecurityConfiguracao.
 * Quem vai usar: o JwtDecoder (chave) e, no login, o JwtEncoder (chave e minutos de validade).
 */
@ConfigurationProperties(prefix = "seguranca.jwt")
public record JwtPropriedades(String chave, long expiracaoMinutos) {
}
