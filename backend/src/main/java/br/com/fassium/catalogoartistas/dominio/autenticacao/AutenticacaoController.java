package br.com.fassium.catalogoartistas.dominio.autenticacao;

import br.com.fassium.catalogoartistas.dominio.autenticacao.dto.LoginRequest;
import br.com.fassium.catalogoartistas.dominio.autenticacao.dto.TokenResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/autenticacao")
public class AutenticacaoController {

    private final AutenticacaoService autenticacaoService;

    public AutenticacaoController(AutenticacaoService autenticacaoService) {
        this.autenticacaoService = autenticacaoService;
    }

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest request) {
        return autenticacaoService.login(request);
    }
}
