package br.com.fassium.catalogoartistas.infra.configuracao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

/**
 * Configuração da paginação: formato do JSON de páginas e tamanho máximo de página.
 * <p>
 * O formato do JSON vem da anotação @EnableSpringDataWebSupport; o limite de tamanho vem do bean abaixo.
 * Quando um Service devolve um Page<...> (ex.: GET /v1/artistas), o Spring converte para JSON.
 * Com VIA_DTO, o JSON sai num formato estável: "content" (os itens) e "page" (número, tamanho, total).
 * Sem esta classe, o Spring avisa que o formato padrão do Page não é garantido entre versões.
 */
@Configuration
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class PaginacaoConfiguracao {

    private static final int TAMANHO_MAXIMO_PAGINA = 100;

    /**
     * Limita o "size" que o cliente pode pedir (?size=5000 vira 100), para ninguém carregar a tabela inteira de uma vez.
     * Não usamos a propriedade spring.data.web.pageable.max-page-size porque, com @EnableSpringDataWebSupport
     * declarado aqui, ela não era aplicada (o limite continuava 2000).
     */
    @Bean
    public PageableHandlerMethodArgumentResolverCustomizer limitarTamanhoPagina() {
        return resolver -> resolver.setMaxPageSize(TAMANHO_MAXIMO_PAGINA);
    }
}
