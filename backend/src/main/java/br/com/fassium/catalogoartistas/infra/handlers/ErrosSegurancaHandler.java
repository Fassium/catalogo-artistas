package br.com.fassium.catalogoartistas.infra.handlers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

/**
 * Erros 401 e 403 da segurança, no mesmo formato ProblemDetail do resto da API.
 * <p>
 * Por que não está no GlobalExceptionHandler: a segurança roda num filtro, ANTES do Controller, então o
 * @RestControllerAdvice nunca vê esses erros. O Spring Security pede dois ganchos, e esta classe implementa os dois:
 * - AuthenticationEntryPoint (401): sem token, token adulterado ou vencido.
 * - AccessDeniedHandler (403): autenticado, mas sem permissão para a rota.
 * Ligada no SecurityConfiguracao.
 */
@Component
public class ErrosSegurancaHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    public ErrosSegurancaHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException ex) throws IOException {
        response.setHeader("WWW-Authenticate", "Bearer");
        escrever(request, response, HttpStatus.UNAUTHORIZED, "Token ausente, inválido ou expirado.");
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException ex) throws IOException {
        escrever(request, response, HttpStatus.FORBIDDEN, "Você não tem permissão para acessar este recurso.");
    }

    private void escrever(HttpServletRequest request, HttpServletResponse response,
            HttpStatus status, String detalhe) throws IOException {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(status, detalhe);
        problema.setInstance(URI.create(request.getRequestURI()));

        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        objectMapper.writeValue(response.getOutputStream(), problema);
    }

}
