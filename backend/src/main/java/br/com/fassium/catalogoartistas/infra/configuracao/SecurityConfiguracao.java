package br.com.fassium.catalogoartistas.infra.configuracao;

import br.com.fassium.catalogoartistas.infra.handlers.ErrosSegurancaHandler;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
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
import java.util.List;
import java.util.Base64;

/**
 * Segurança da API: quais rotas são públicas, como o token JWT é conferido e criado, e o CORS.
 * Toda requisição passa pelo filtro de filtroSeguranca() antes de chegar ao Controller.
 */
@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(JwtPropriedades.class)
public class SecurityConfiguracao {

    private static final int TAMANHO_MINIMO_CHAVE_BYTES = 32;

    /**
     * Gera e confere hash BCrypt de senhas. Usado pelo AutenticacaoService no login.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Monta o filtro de segurança que o Spring aplica a todas as requisições:
     * rotas públicas (login, refresh, Swagger) passam direto; as demais exigem "Authorization: Bearer <token>".
     * Token ausente, inválido ou vencido gera 401, e falta de permissão gera 403, ambos em ProblemDetail
     * (ErrosSegurancaHandler). Sem sessão (STATELESS) e sem CSRF, pois a API autentica só por token.
     * A ordem das regras importa: vale a primeira que combina com a rota.
     */
    @Bean
    public SecurityFilterChain filtroSeguranca(HttpSecurity http, ErrosSegurancaHandler errosSeguranca) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sessao -> sessao.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(rotas -> rotas
                        .requestMatchers(HttpMethod.POST, "/v1/autenticacao/login").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/v1/autenticacao/refresh").permitAll()
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(erros -> erros
                        .authenticationEntryPoint(errosSeguranca)
                        .accessDeniedHandler(errosSeguranca))
                .oauth2ResourceServer(recurso -> recurso
                        .jwt(Customizer.withDefaults())
                        .authenticationEntryPoint(errosSeguranca)
                        .accessDeniedHandler(errosSeguranca));

        return http.build();
    }

    /**
     * Define quais origens (sites) o navegador pode deixar chamar a API, como o Angular em localhost:4200.
     * As origens vêm de seguranca.cors.origens-permitidas (CORS_ORIGENS, separadas por vírgula), nunca "*".
     * O .cors() do filtroSeguranca usa este bean e responde o preflight (OPTIONS) antes da checagem de token.
     * CORS é regra só de navegador: não afeta o curl nem o front Delphi.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource(
            @Value("${seguranca.cors.origens-permitidas}") List<String> origensPermitidas) {
        CorsConfiguration configuracao = new CorsConfiguration();
        configuracao.setAllowedOrigins(origensPermitidas);
        configuracao.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuracao.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuracao.setMaxAge(Duration.ofHours(1));

        UrlBasedCorsConfigurationSource fonte = new UrlBasedCorsConfigurationSource();
        fonte.registerCorsConfiguration("/**", configuracao);
        return fonte;
    }

    /**
     * Monta a chave secreta do JWT (HS256) a partir de JWT_CHAVE (Base64), uma vez só, para o jwtEncoder e o jwtDecoder.
     * Se a chave não for Base64 válido ou tiver menos de 32 bytes, a API não sobe.
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
     * Confere os tokens recebidos: assinatura (com a chaveJwt), validade (tolerância de 5 s) e emissor.
     * Chamado pelo Spring Security em cada requisição protegida; nós não chamamos.
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
     * Assina os tokens novos com a mesma chaveJwt do jwtDecoder (quem assina e quem confere usam a mesma chave).
     * Chamado pelo AutenticacaoService no login e no refresh.
     */
    @Bean
    public JwtEncoder jwtEncoder(SecretKey chaveJwt) {
        return new NimbusJwtEncoder(new ImmutableSecret<>(chaveJwt));
    }

}
