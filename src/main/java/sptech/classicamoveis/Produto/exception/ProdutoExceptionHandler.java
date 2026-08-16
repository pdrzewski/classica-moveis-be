package sptech.classicamoveis.Produto.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import sptech.classicamoveis.Produto.controller.ProdutoController;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice(assignableTypes = ProdutoController.class)
public class ProdutoExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ProdutoErroResponse> tratarRecursoNaoEncontrado(
            RecursoNaoEncontradoException exception, HttpServletRequest request) {
        return resposta(HttpStatus.NOT_FOUND, exception.getMessage(), request, Map.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProdutoErroResponse> tratarValidacao(
            MethodArgumentNotValidException exception, HttpServletRequest request) {
        Map<String, String> campos = new LinkedHashMap<>();
        exception.getBindingResult().getAllErrors().forEach(erro -> {
            String campo = erro instanceof FieldError fieldError ? fieldError.getField() : erro.getObjectName();
            campos.put(campo, erro.getDefaultMessage());
        });

        return resposta(HttpStatus.BAD_REQUEST, "Existem campos inválidos na requisição.", request, campos);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ProdutoErroResponse> tratarRequisicaoInvalida(Exception exception,
                                                                         HttpServletRequest request) {
        return resposta(HttpStatus.BAD_REQUEST, "A requisição possui dados inválidos ou mal formatados.", request, Map.of());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProdutoErroResponse> tratarViolacaoDeIntegridade(
            DataIntegrityViolationException exception, HttpServletRequest request) {
        return resposta(HttpStatus.CONFLICT,
                "Não foi possível concluir a operação porque o produto possui dados relacionados.",
                request,
                Map.of());
    }

    private ResponseEntity<ProdutoErroResponse> resposta(HttpStatus status,
                                                          String mensagem,
                                                          HttpServletRequest request,
                                                          Map<String, String> campos) {
        ProdutoErroResponse erro = new ProdutoErroResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                mensagem,
                request.getRequestURI(),
                campos
        );
        return ResponseEntity.status(status).body(erro);
    }
}
