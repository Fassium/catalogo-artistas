package br.com.fassium.catalogoartistas.infra.configuracao;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
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

    /**
     * Cria o "cabeçalho" da documentação: título, descrição e versão que aparecem no topo do Swagger.
     * Não tem regra de negócio, só texto.
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Catálogo de Artista e Álbuns")
                        .description("API de treino: artistas, álbuns e seus vínculos")
                        .version("v1"));
    }

}
