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

/**
 * Login e renovação de tokens. O access token é um JWT de curta duração; o refresh token é um texto aleatório
 * de 7 dias, guardado no banco só como hash e usado uma única vez (a cada renovação sai um par novo).
 */
@Service
public class AutenticacaoService {

    private static final long REFRESH_DIAS = 7;
    // Hash BCrypt válido, usado só para igualar o tempo de resposta quando o e-mail não existe.
    private static final String HASH_FICTICIO = "$2a$10$Y7zuYshgxcVaYWITxjtqTukY4qKh40BN77YkdKn9MK7Gsm/CSai8O";

    private final UsuarioRepository usuarioRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;
    private final JwtPropriedades jwtPropriedades;
    private final SecureRandom secureRandom = new SecureRandom();

    public AutenticacaoService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtEncoder jwtEncoder, JwtPropriedades jwtPropriedades, RefreshTokenRepository refreshTokenRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
        this.jwtPropriedades = jwtPropriedades;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    /**
     * Valida e-mail e senha e devolve o usuário. E-mail inexistente, senha errada e usuário inativo lançam a mesma
     * exceção. Com e-mail inexistente, roda o BCrypt contra um hash fictício para que o tempo de resposta
     * não revele quais e-mails existem.
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

    /**
     * Cria o JWT assinado (HS256) com id (sub), e-mail, emissor e validade vindos do JwtPropriedades.
     */
    private String gerarAccessToken(Usuario usuario) {
        Instant agora = Instant.now();

        JwtClaimsSet dados = JwtClaimsSet.builder()
                .subject(String.valueOf(usuario.getId()))
                .claim("email", usuario.getEmail())
                .issuer(jwtPropriedades.emissor())
                .issuedAt(agora)
                .expiresAt(agora.plus(jwtPropriedades.expiracaoMinutos(), ChronoUnit.MINUTES))
                .build();

        JwtEncoderParameters parametros = JwtEncoderParameters.from(
                JwsHeader.with(MacAlgorithm.HS256).build(), dados);

        return jwtEncoder.encode(parametros).getTokenValue();
    }

    /**
     * Gera o refresh token: 32 bytes aleatórios em Base64 URL-safe. Não é JWT, não carrega dados.
     */
    private String gerarRefreshTokenOpaco() {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    /**
     * SHA-256 em hexadecimal. O banco guarda só o hash do refresh token, nunca o token em si.
     */
    private String calcularHash(String valor) {
        try {
            byte[] resumo = MessageDigest.getInstance("SHA-256").digest(valor.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(resumo);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 indisponível", e);
        }
    }

    /**
     * Valida as credenciais e devolve access token + refresh token.
     */
    @Transactional
    public TokenResponse login(LoginRequest request) {
        return emitirTokens(validarCredenciais(request));
    }

    /**
     * Gera o par de tokens e salva o hash do refresh token com validade de 7 dias.
     * Usado pelo login e pela renovação.
     */
    private TokenResponse emitirTokens(Usuario usuario) {
        String accessToken = gerarAccessToken(usuario);
        String refreshToken = gerarRefreshTokenOpaco();

        refreshTokenRepository.save(new RefreshToken(usuario, calcularHash(refreshToken), Instant.now().plus(REFRESH_DIAS, ChronoUnit.DAYS)));

        return TokenResponse.bearer(accessToken, refreshToken, jwtPropriedades.expiracaoMinutos() * 60);
    }

    /**
     * Troca um refresh token válido por um par novo e revoga o antigo (rotação).
     * Se o token já estava revogado, alguém o reutilizou: revoga todos os tokens do usuário.
     * O noRollbackFor mantém essa revogação gravada mesmo com a exceção lançada em seguida.
     */
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
