package br.com.fassium.catalogoartistas.infra.configuracao;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configura a documentação Swagger (/swagger-ui.html). O SpringDoc lê o bean abaixo ao subir a API.
 */
@Configuration
public class OpenApiConfiguracao {

    private static final String ESQUEMA_BEARER = "bearerAuth";

    /**
     * Define título, descrição e versão da API e o esquema Bearer JWT, que habilita o botão "Authorize".
     * O cadeado vale para todas as rotas; as públicas (login e refresh) o removem com @SecurityRequirements.
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
