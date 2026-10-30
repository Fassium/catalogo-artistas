package br.com.fassium.catalogoartistas.dominio.autenticacao.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshRequest(@NotBlank String refreshToken) {
}
