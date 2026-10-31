package br.com.fassium.catalogoartistas.infra.configuracao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Configurações do JWT lidas do bloco "seguranca.jwt" do application.yaml (a chave vem da variável JWT_CHAVE).
 * Registrada no SecurityConfiguracao; injetada em quem precisa da chave, do emissor ou da validade do token.
 * A validação (@NotBlank, @Positive) faz a API não subir se algo estiver ausente ou inválido.
 */
@Validated
@ConfigurationProperties(prefix = "seguranca.jwt")
public record JwtPropriedades(
        @NotBlank String chave,
        @Positive long expiracaoMinutos,
        @NotBlank String emissor) {
}
