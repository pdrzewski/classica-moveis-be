package sptech.classicamoveis.config;

import jakarta.persistence.EntityNotFoundException;
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

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice(basePackages = "sptech.classicamoveis")
public class ApiExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> tratarNaoEncontrado(EntityNotFoundException ex, HttpServletRequest request) {
        return resposta(HttpStatus.NOT_FOUND, ex.getMessage(), request, Map.of());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> tratarArgumentoInvalido(IllegalArgumentException ex, HttpServletRequest request) {
        return resposta(HttpStatus.BAD_REQUEST, ex.getMessage(), request, Map.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> tratarValidacao(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> campos = new LinkedHashMap<>();
        for (FieldError erro : ex.getBindingResult().getFieldErrors()) {
            campos.put(erro.getField(), erro.getDefaultMessage());
        }
        return resposta(HttpStatus.BAD_REQUEST, "Existem campos inválidos na requisição.", request, campos);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Map<String, Object>> tratarFormatoInvalido(Exception ex, HttpServletRequest request) {
        return resposta(HttpStatus.BAD_REQUEST, "A requisição contém dados inválidos ou mal formatados.", request, Map.of());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> tratarIntegridade(DataIntegrityViolationException ex, HttpServletRequest request) {
        return resposta(HttpStatus.CONFLICT,
                "Não foi possível concluir a operação porque este registro está relacionado a outros dados. Verifique os vínculos antes de excluir ou alterar.",
                request, Map.of());
    }

    private ResponseEntity<Map<String, Object>> resposta(HttpStatus status, String mensagem,
                                                          HttpServletRequest request, Map<String, String> campos) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", status.value());
        body.put("erro", status.getReasonPhrase());
        body.put("mensagem", mensagem == null ? status.getReasonPhrase() : mensagem);
        body.put("caminho", request.getRequestURI());
        if (!campos.isEmpty()) body.put("campos", campos);
        return ResponseEntity.status(status).body(body);
    }
}
