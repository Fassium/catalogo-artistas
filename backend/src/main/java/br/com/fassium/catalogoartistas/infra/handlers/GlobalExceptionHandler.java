package br.com.fassium.catalogoartistas.infra.handlers;

import br.com.fassium.catalogoartistas.common.exceptions.CredenciaisInvalidasException;
import br.com.fassium.catalogoartistas.common.exceptions.RecursoNaoEncontradoException;
import br.com.fassium.catalogoartistas.common.exceptions.RefreshTokenInvalidoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException; // NOVO
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tools.jackson.databind.exc.InvalidFormatException;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ProblemDetail tratarNaoEncontrado(RecursoNaoEncontradoException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail tratarCorpoIlegivel(HttpMessageNotReadableException ex) {
        String detalhe = "Corpo da requisição inválido.";

        if (ex.getCause() instanceof InvalidFormatException causa && !causa.getPath().isEmpty()) {
            String campo = causa.getPath().getLast().getPropertyName();
            detalhe = "Valor inválido para o campo '" + campo + "'.";
        }

        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detalhe);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail tratarValidacao(MethodArgumentNotValidException ex) {
        List<Map<String, String>> camposInvalidos = ex.getBindingResult().getFieldErrors().stream()
                .map(erro -> Map.of(
                        "campo", erro.getField(),
                        "mensagem", String.valueOf(erro.getDefaultMessage())))
                .toList();

        ProblemDetail problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNPROCESSABLE_CONTENT, "Há campos inválidos na requisição.");
        problema.setProperty("camposInvalidos", camposInvalidos);
        return problema;
    }

    @ExceptionHandler(CredenciaisInvalidasException.class)
    public ProblemDetail tratarCredenciaisInvalidas(CredenciaisInvalidasException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(RefreshTokenInvalidoException.class)
    public ProblemDetail tratarRefreshTokenInvalido(RefreshTokenInvalidoException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }


}
