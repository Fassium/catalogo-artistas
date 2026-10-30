package br.com.fassium.catalogoartistas.infra.configuracao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Espelho do bloco "seguranca.jwt" do application.yaml, como objeto Java.
 * <p>
 * Fluxo: .env (JWT_CHAVE) -> application.yaml (chave: ${JWT_CHAVE}) -> esta classe -> quem precisar injeta.
 * O Spring liga "expiracao-minutos" do YAML ao campo expiracaoMinutos sozinho.
 * Registrada pelo @EnableConfigurationProperties do SecurityConfiguracao.
 * Quem vai usar: o JwtDecoder (chave) e, no login, o JwtEncoder (chave e minutos de validade).
 */

@Validated
@ConfigurationProperties(prefix = "seguranca.jwt")
public record JwtPropriedades(@NotBlank String chave, @Positive long expiracaoMinutos) {
}
