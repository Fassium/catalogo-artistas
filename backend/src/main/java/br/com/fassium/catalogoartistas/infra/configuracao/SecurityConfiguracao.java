package br.com.fassium.catalogoartistas.infra.configuracao;

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
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
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
     * O "conferidor" de tokens (JWT = JSON Web Token: um texto com 3 partes, cabeçalho.dados.assinatura).
     * <p>
     * Quem chama: o Spring Security, sozinho, a cada pedido a uma rota protegida (por causa do
     * oauth2ResourceServer acima). Nós não chamamos este método.
     * <p>
     * O que faz, em ordem:
     * 1. Recebe o JwtPropriedades (o Spring injeta; veio do .env -> application.yaml).
     * 2. Base64.decode: a chave no .env é um texto; aqui vira os bytes reais da chave.
     * 3. SecretKeySpec: embrulha os bytes como chave do algoritmo HmacSHA256 (HS256).
     * 4. NimbusJwtDecoder: monta o conferidor. Ele recalcula a assinatura do token com ESTA chave:
     * se bater, ninguém mexeu nos dados; se não bater, o token é recusado. Também confere a validade (exp).
     * <p>
     * O JwtEncoder (a criar) vai assinar o token no login com a MESMA chave. É isso que liga as duas pontas:
     * quem assina e quem confere precisam da mesma chave. Se a chave mudar, todos os tokens antigos deixam de valer.
     */
    @Bean
    public JwtDecoder jwtDecoder(JwtPropriedades propriedades) {
        SecretKey chave = new SecretKeySpec(Base64.getDecoder().decode(propriedades.chave()), "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(chave).build();
    }


}
