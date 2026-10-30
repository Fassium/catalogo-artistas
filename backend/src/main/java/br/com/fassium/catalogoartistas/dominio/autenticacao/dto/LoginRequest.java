package br.com.fassium.catalogoartistas.dominio.autenticacao.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank String email,
        @NotBlank String senha) {
}
