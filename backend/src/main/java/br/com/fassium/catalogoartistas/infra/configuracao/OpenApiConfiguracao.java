package br.com.fassium.catalogoartistas.infra.configuracao;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguracao {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Catálogo de Artista e Álbuns")
                        .description("API de treino: artistas, álbuns e seus vínculos")
                        .version("v1"));
    }

}
