package br.com.fassium.catalogoartistas.infra.configuracao;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

/**
 * Configuração do formato do JSON de páginas.
 * <p>
 * Não tem método: a anotação @EnableSpringDataWebSupport faz todo o trabalho.
 * Quando um Service devolve um Page<...> (ex.: GET /v1/artistas), o Spring converte para JSON.
 * Com VIA_DTO, o JSON sai num formato estável: "content" (os itens) e "page" (número, tamanho, total).
 * Sem esta classe, o Spring avisa que o formato padrão do Page não é garantido entre versões.
 */
@Configuration
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class PaginacaoConfiguracao {
}
