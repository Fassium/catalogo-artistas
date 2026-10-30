package br.com.fassium.catalogoartistas.infra.configuracao;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Duration;
import java.util.Base64;

/**
 * Configuração da segurança: quem pode chamar qual rota.
 * <p>
 * Ordem de uma requisição:
 * 1. O pedido chega do cliente (Postman, Angular).
 * 2. O Spring Security passa o pedido pelo filtro montado em filtroSeguranca() ANTES do Controller.
 * 3. Rota liberada (login, refresh, Swagger): segue direto para o Controller.
 * 4. Qualquer outra rota: exige token. Sem token, o pedido é barrado (hoje 403) e o Controller nem é chamado.
 * <p>
 * EnableWebSecurity liga o Spring Security.
 * EnableConfigurationProperties registra o JwtPropriedades, que lê o bloco "seguranca.jwt" do application.yaml
 * (a chave vem do .env pela variável JWT_CHAVE).
 * O JwtDecoder confere a assinatura e a validade do token em cada requisição protegida.
 */
@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(JwtPropriedades.class)
public class SecurityConfiguracao {

    private static final int TAMANHO_MINIMO_CHAVE_BYTES = 32;

    /**
     * Ferramenta que embaralha senhas (BCrypt). Será usada no login: a senha digitada é comparada
     * com o hash guardado na tabela usuario. A senha nunca é guardada nem comparada em texto puro.
     * Quem chama (no futuro): o AutenticacaoService, no login.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * As regras de segurança, lidas de cima para baixo:
     * csrf desligado: proteção contra formulário de site, que não se aplica a uma API com token.
     * sessão STATELESS: a API não guarda "quem está logado"; cada pedido tem de trazer o token.
     * authorizeHttpRequests: lista de quem passa sem token (login, refresh, Swagger) e a regra final,
     * anyRequest().authenticated(), que manda todo o resto exigir token.
     * A ordem importa: o Spring usa a primeira regra que combina com a rota.
     * oauth2ResourceServer + jwt: manda o Spring ler o header "Authorization: Bearer <token>" de cada pedido
     * e conferir o token usando o bean JwtDecoder (abaixo). Token válido: a requisição segue. Token ausente,
     * adulterado ou vencido (mais de 5 min): o Spring responde 401 e o Controller nem é chamado.
     * Customizer.withDefaults() = "use o comportamento padrão", sem ajustes extras.
     * http.build() monta o filtro, e o Spring o coloca na frente de todos os Controllers.
     */
    @Bean
    public SecurityFilterChain filtroSeguranca(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sessao -> sessao.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(rotas -> rotas
                        .requestMatchers(HttpMethod.POST, "/v1/autenticacao/login").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/v1/autenticacao/refresh").permitAll()
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated())
                .oauth2ResourceServer(recurso -> recurso.jwt(Customizer.withDefaults()));

        return http.build();
    }

    /**
     * A chave secreta do JWT (HS256), montada UMA vez só e usada pelo jwtDecoder e pelo jwtEncoder.
     * <p>
     * Quem chama: o Spring, na subida da API, para entregar a chave aos dois beans abaixo.
     * <p>
     * O que faz, em ordem:
     * 1. Base64.decode: a chave no .env é um texto; aqui vira os bytes reais. Se não for Base64 válido
     * (ex.: JWT_CHAVE ausente), a API NÃO sobe e a mensagem diz o motivo.
     * 2. Confere o tamanho: o HS256 exige no mínimo 32 bytes (256 bits). Chave curta = a API não sobe.
     * Falhar na subida é melhor que descobrir no primeiro login, com erro 500.
     * 3. SecretKeySpec: embrulha os bytes como chave do algoritmo HmacSHA256.
     */
    @Bean
    public SecretKey chaveJwt(JwtPropriedades propriedades) {
        byte[] bytes;
        try {
            bytes = Base64.getDecoder().decode(propriedades.chave());
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("A chave JWT (JWT_CHAVE) não é um Base64 válido.", e);
        }

        if (bytes.length < TAMANHO_MINIMO_CHAVE_BYTES) {
            throw new IllegalStateException("A chave JWT (JWT_CHAVE) tem " + bytes.length
                    + " bytes; o HS256 exige no mínimo " + TAMANHO_MINIMO_CHAVE_BYTES + ".");
        }

        return new SecretKeySpec(bytes, "HmacSHA256");
    }

    /**
     * O "conferidor" de tokens (JWT = JSON Web Token: um texto com 3 partes, cabeçalho.dados.assinatura).
     * <p>
     * Quem chama: o Spring Security, sozinho, a cada pedido a uma rota protegida (por causa do
     * oauth2ResourceServer acima). Nós não chamamos este método.
     * <p>
     * O que faz, em ordem:
     * 1. Recebe a SecretKey pronta do bean chaveJwt (já decodificada e validada na subida).
     * 2. NimbusJwtDecoder: monta o conferidor. Ele recalcula a assinatura do token com ESTA chave:
     * se bater, ninguém mexeu nos dados; se não bater, o token é recusado. Também confere a validade (exp).
     * <p>
     * O JwtEncoder (abaixo) assina o token no login com a MESMA chave. É isso que liga as duas pontas:
     * quem assina e quem confere precisam da mesma chave. Se a chave mudar, todos os tokens antigos deixam de valer.
     */
    @Bean
    public JwtDecoder jwtDecoder(SecretKey chaveJwt, JwtPropriedades propriedades) {
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withSecretKey(chaveJwt).build();

        decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(
                new JwtTimestampValidator(Duration.ofSeconds(5)),
                new JwtIssuerValidator(propriedades.emissor())));

        return decoder;
    }


    /**
     * O "criador" de tokens: o par do JwtDecoder. Ele ASSINA o token; o decoder CONFERE.
     * <p>
     * Quem chama: o nosso AutenticacaoService (a criar), no login e no refresh. Ele monta os dados
     * (e-mail do usuário, hora de criação, validade de 5 min) e pede para este encoder gerar o texto do token.
     * Diferente do decoder, aqui NÓS chamamos, quando precisamos de um token novo.
     * <p>
     * O que faz, em ordem:
     * 1. Recebe a MESMA SecretKey do decoder (bean chaveJwt): a chave é montada uma vez só.
     * 2. ImmutableSecret: embrulha a chave no formato que a biblioteca Nimbus pede para assinar.
     * 3. NimbusJwtEncoder: monta o criador. Ele junta cabeçalho + dados, calcula a assinatura com a chave
     * e devolve o texto "cabeçalho.dados.assinatura".
     * <p>
     * Fluxo completo: login -> AutenticacaoService -> jwtEncoder cria o token -> cliente guarda ->
     * próximos pedidos levam o token -> jwtDecoder confere. Chaves diferentes = token recusado (401).
     */
    @Bean
    public JwtEncoder jwtEncoder(SecretKey chaveJwt) {
        return new NimbusJwtEncoder(new ImmutableSecret<>(chaveJwt));
    }

}
