package br.com.fassium.catalogoartistas.dominio.autenticacao;

import br.com.fassium.catalogoartistas.dominio.autenticacao.dto.LoginRequest;
import br.com.fassium.catalogoartistas.dominio.autenticacao.dto.RefreshRequest;
import br.com.fassium.catalogoartistas.dominio.autenticacao.dto.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Autenticação", description = "Login, renovação de tokens e logout (rotas públicas)")
@RestController
@RequestMapping("/v1/autenticacao")
public class AutenticacaoController {

    private final AutenticacaoService autenticacaoService;

    public AutenticacaoController(AutenticacaoService autenticacaoService) {
        this.autenticacaoService = autenticacaoService;
    }

    @Operation(summary = "Login",
            description = "Valida e-mail e senha e devolve o access token (JWT, 5 min) e o refresh token (1 dia).")
    @SecurityRequirements
    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest request) {
        return autenticacaoService.login(request);
    }

    @Operation(summary = "Renovar tokens",
            description = "Troca o refresh token por um par novo de tokens. Cada refresh token vale uma vez só; "
                    + "reusar um já usado revoga todos os refresh tokens do usuário.")
    @SecurityRequirements
    @PutMapping("/refresh")
    public TokenResponse refresh(@Valid @RequestBody RefreshRequest request) {
        return autenticacaoService.renovar(request);
    }

    @Operation(summary = "Logout",
            description = "Revoga o refresh token informado. Sempre 204, mesmo se o token não existir ou já estiver revogado. "
                    + "O access token continua válido até expirar (5 min); o front deve descartá-lo.")
    @SecurityRequirements
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody RefreshRequest request) {
        autenticacaoService.logout(request);
        return ResponseEntity.noContent().build();
    }
}
