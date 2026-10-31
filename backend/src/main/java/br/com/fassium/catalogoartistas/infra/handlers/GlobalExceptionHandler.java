package br.com.fassium.catalogoartistas.infra.handlers;

import br.com.fassium.catalogoartistas.common.exceptions.CredenciaisInvalidasException;
import br.com.fassium.catalogoartistas.common.exceptions.OrdenacaoInvalidaException;
import br.com.fassium.catalogoartistas.common.exceptions.RecursoNaoEncontradoException;
import br.com.fassium.catalogoartistas.common.exceptions.RefreshTokenInvalidoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.core.PropertyReferenceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import tools.jackson.databind.exc.InvalidFormatException;

import java.util.List;
import java.util.Map;

/**
 * Traduz toda exceção da API para um ProblemDetail (RFC 9457), o mesmo formato em todos os erros.
 * <p>
 * Estende ResponseEntityExceptionHandler: a classe base já trata os erros que o próprio Spring gera
 * (id não numérico, rota inexistente, método HTTP errado, tipo de conteúdo não aceito) no formato ProblemDetail.
 * Aqui sobrescrevemos só o que queremos customizar e acrescentamos os erros do nosso domínio.
 * Os erros 401/403 da segurança (filtro, antes do Controller) estão em ErrosSegurancaHandler.
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ProblemDetail tratarNaoEncontrado(RecursoNaoEncontradoException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String detalhe = "Corpo da requisição inválido.";

        if (ex.getCause() instanceof InvalidFormatException causa && !causa.getPath().isEmpty()) {
            String campo = causa.getPath().getLast().getPropertyName();
            detalhe = "Valor inválido para o campo '" + campo + "'.";
        }

        ProblemDetail problema = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detalhe);
        return handleExceptionInternal(ex, problema, headers, HttpStatus.BAD_REQUEST, request);
    }

    /**
     * Dois casos chegam aqui:
     * - regra de validação violada (@NotBlank, @Size...) no corpo: 422, com a mensagem da regra;
     * - valor que nem converte para o tipo (?artistaId=abc, ?tipoArtista=XYZ): 400, como no path e no corpo.
     *   A mensagem do Spring nesse caso cita classes Java, então vai só "Valor inválido.".
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<FieldError> erros = ex.getBindingResult().getFieldErrors();
        boolean erroDeConversao = erros.stream().anyMatch(FieldError::isBindingFailure);

        List<Map<String, String>> camposInvalidos = erros.stream()
                .map(erro -> Map.of(
                        "campo", erro.getField(),
                        "mensagem", erro.isBindingFailure()
                                ? "Valor inválido."
                                : String.valueOf(erro.getDefaultMessage())))
                .toList();

        HttpStatus statusResposta = erroDeConversao ? HttpStatus.BAD_REQUEST : HttpStatus.UNPROCESSABLE_CONTENT;
        String detalhe = erroDeConversao ? "Há parâmetros com valor inválido." : "Há campos inválidos na requisição.";

        ProblemDetail problema = ProblemDetail.forStatusAndDetail(statusResposta, detalhe);
        problema.setProperty("camposInvalidos", camposInvalidos);
        return handleExceptionInternal(ex, problema, headers, statusResposta, request);
    }

    @ExceptionHandler(OrdenacaoInvalidaException.class)
    public ProblemDetail tratarOrdenacaoNaoPermitida(OrdenacaoInvalidaException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(CredenciaisInvalidasException.class)
    public ProblemDetail tratarCredenciaisInvalidas(CredenciaisInvalidasException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(RefreshTokenInvalidoException.class)
    public ProblemDetail tratarRefreshTokenInvalido(RefreshTokenInvalidoException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    /** Ordenação por propriedade que não existe (ex.: ?sort=xyz). Sem isto, vira 500. */
    @ExceptionHandler(PropertyReferenceException.class)
    public ProblemDetail tratarOrdenacaoInvalida(PropertyReferenceException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                "Propriedade de ordenação inválida: '" + ex.getPropertyName() + "'.");
    }

    /** Violação de chave única, chave estrangeira etc. A causa técnica vai só para o log, nunca para o cliente. */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail tratarIntegridade(DataIntegrityViolationException ex) {
        log.warn("Violação de integridade de dados", ex);
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,
                "A operação viola uma regra de integridade dos dados.");
    }

    /** Rede de segurança: qualquer erro inesperado. Detalhe interno só no log, o cliente recebe mensagem genérica. */
    @ExceptionHandler(Exception.class)
    public ProblemDetail tratarErroInesperado(Exception ex) {
        log.error("Erro inesperado", ex);
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro interno do servidor.");
    }

}
