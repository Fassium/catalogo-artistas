package br.com.fassium.catalogoartistas.infra.configuracao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

/**
 * Configura a paginação: formato do JSON de um Page e tamanho máximo de página.
 */
@Configuration
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class PaginacaoConfiguracao {

    private static final int TAMANHO_MAXIMO_PAGINA = 100;

    /**
     * Limita o "size" pedido pelo cliente (?size=5000 vira 100). Feito por bean porque a propriedade
     * spring.data.web.pageable.max-page-size não é aplicada com @EnableSpringDataWebSupport.
     */
    @Bean
    public PageableHandlerMethodArgumentResolverCustomizer limitarTamanhoPagina() {
        return resolver -> resolver.setMaxPageSize(TAMANHO_MAXIMO_PAGINA);
    }
}
