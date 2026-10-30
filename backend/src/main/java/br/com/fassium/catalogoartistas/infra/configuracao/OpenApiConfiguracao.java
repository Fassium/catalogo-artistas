package br.com.fassium.catalogoartistas.infra.configuracao;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração da documentação (Swagger).
 * <p>
 * Quem chama: ninguém no nosso código. O Spring, ao subir a API, acha o @Configuration,
 * executa o método @Bean e entrega o objeto ao SpringDoc, que monta a página /swagger-ui.html.
 * Ordem: Spring sobe -> lê esta classe -> SpringDoc usa o OpenAPI para escrever o título da página.
 */
@Configuration
public class OpenApiConfiguracao {

    private static final String ESQUEMA_BEARER = "bearerAuth";

    /**
     * Cria o "cabeçalho" da documentação: título, descrição e versão que aparecem no topo do Swagger.
     * <p>
     * Também declara o esquema de segurança Bearer JWT: é ele que faz aparecer o botão "Authorize" no Swagger.
     * O avaliador faz login, cola o accessToken ali e todas as rotas protegidas passam a ser testáveis.
     * addSecurityItem aplica o cadeado a TODAS as rotas; login e refresh o desligam com @SecurityRequirements
     * (vazio) no AutenticacaoController, porque são públicos.
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Catálogo de Artista e Álbuns")
                        .description("API de treino: artistas, álbuns e seus vínculos")
                        .version("v1"))
                .components(new Components().addSecuritySchemes(ESQUEMA_BEARER,
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList(ESQUEMA_BEARER));
    }

}
