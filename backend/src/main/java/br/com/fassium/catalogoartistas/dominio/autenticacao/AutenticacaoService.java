package br.com.fassium.catalogoartistas.dominio.autenticacao;

import br.com.fassium.catalogoartistas.common.exceptions.CredenciaisInvalidasException;
import br.com.fassium.catalogoartistas.common.exceptions.RefreshTokenInvalidoException;
import br.com.fassium.catalogoartistas.dominio.autenticacao.dto.LoginRequest;
import br.com.fassium.catalogoartistas.dominio.autenticacao.dto.RefreshRequest;
import br.com.fassium.catalogoartistas.dominio.autenticacao.dto.TokenResponse;
import br.com.fassium.catalogoartistas.dominio.autenticacao.model.RefreshToken;
import br.com.fassium.catalogoartistas.dominio.autenticacao.model.Usuario;
import br.com.fassium.catalogoartistas.infra.configuracao.JwtPropriedades;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Locale;

@Service
public class AutenticacaoService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;
    private final JwtPropriedades jwtPropriedades;
    private final SecureRandom secureRandom = new SecureRandom();
    private static final long REFRESH_DIAS = 7;
    private final RefreshTokenRepository refreshTokenRepository;
    // BCrypt válido usado só para gastar tempo quando o e-mail não existe (ver validarCredenciais).
    private static final String HASH_FICTICIO = "$2a$10$Y7zuYshgxcVaYWITxjtqTukY4qKh40BN77YkdKn9MK7Gsm/CSai8O";


    public AutenticacaoService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtEncoder jwtEncoder, JwtPropriedades jwtPropriedades, RefreshTokenRepository refreshTokenRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
        this.jwtPropriedades = jwtPropriedades;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    /**
     * Valida e-mail e senha. E-mail inexistente e senha errada lançam a MESMA exceção.
     * <p>
     * Defesa contra enumeração de usuários por tempo de resposta: o BCrypt é lento de propósito,
     * então e-mail inexistente (sem BCrypt) responderia bem mais rápido que senha errada.
     * Por isso, quando o e-mail não existe, rodamos o BCrypt contra um hash fictício e
     * descartamos o resultado, só para igualar o tempo dos dois casos.
     */
    private Usuario validarCredenciais(LoginRequest request) {
        String email = request.email().trim().toLowerCase(Locale.ROOT);

        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);

        if (usuario == null) {
            passwordEncoder.matches(request.senha(), HASH_FICTICIO);
            throw new CredenciaisInvalidasException();
        }

        if (!usuario.isAtivo() || !passwordEncoder.matches(request.senha(), usuario.getSenha())) {
            throw new CredenciaisInvalidasException();
        }

        return usuario;

    }
    
    private String gerarAccessToken(Usuario usuario) {
        Instant agora = Instant.now();

        JwtClaimsSet dados = JwtClaimsSet.builder()
                .subject(usuario.getEmail())
                .issuer(jwtPropriedades.emissor())
                .issuedAt(agora)
                .expiresAt(agora.plus(jwtPropriedades.expiracaoMinutos(), ChronoUnit.MINUTES))
                .build();

        JwtEncoderParameters parametros = JwtEncoderParameters.from(
                JwsHeader.with(MacAlgorithm.HS256).build(), dados);

        return jwtEncoder.encode(parametros).getTokenValue();
    }

    private String gerarRefreshTokenOpaco() {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String calcularHash(String valor) {
        try {
            byte[] resumo = MessageDigest.getInstance("SHA-256").digest(valor.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(resumo);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 indisponível", e);
        }
    }

    @Transactional
    public TokenResponse login(LoginRequest request) {
        return emitirTokens(validarCredenciais(request));
    }

    private TokenResponse emitirTokens(Usuario usuario) {
        String accessToken = gerarAccessToken(usuario);
        String refreshToken = gerarRefreshTokenOpaco();

        refreshTokenRepository.save(new RefreshToken(usuario, calcularHash(refreshToken), Instant.now().plus(REFRESH_DIAS, ChronoUnit.DAYS)));

        return TokenResponse.bearer(accessToken, refreshToken, jwtPropriedades.expiracaoMinutos() * 60);
    }

    @Transactional(noRollbackFor = RefreshTokenInvalidoException.class)
    public TokenResponse renovar(RefreshRequest request) {
        RefreshToken atual = refreshTokenRepository.findByHash(calcularHash(request.refreshToken()))
                .orElseThrow(RefreshTokenInvalidoException::new);

        if (atual.isRevogado()) {
            refreshTokenRepository.revogarTodosDoUsuario(atual.getUsuario());
            throw new RefreshTokenInvalidoException();
        }

        if (!atual.estaValido(Instant.now()) || !atual.getUsuario().isAtivo()) {
            throw new RefreshTokenInvalidoException();
        }

        atual.revogar();

        return emitirTokens(atual.getUsuario());
    }


}
