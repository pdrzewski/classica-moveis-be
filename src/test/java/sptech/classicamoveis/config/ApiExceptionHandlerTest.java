package sptech.classicamoveis.config;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApiExceptionHandlerTest {
    private ApiExceptionHandler handler;
    private HttpServletRequest request;

    @BeforeEach
    void configurar() {
        handler = new ApiExceptionHandler();
        request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/teste");
    }

    @Test
    void deveConverterRegistroNaoEncontradoEm404() {
        var resposta = handler.tratarNaoEncontrado(new EntityNotFoundException("Registro não encontrado"), request);
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
        assertEquals("Registro não encontrado", resposta.getBody().get("mensagem"));
        assertEquals("/api/teste", resposta.getBody().get("caminho"));
        assertNotNull(resposta.getBody().get("timestamp"));
    }

    @Test
    void deveConverterArgumentoInvalidoEm400() {
        var resposta = handler.tratarArgumentoInvalido(new IllegalArgumentException("Valor inválido"), request);
        assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
        assertEquals("Valor inválido", resposta.getBody().get("mensagem"));
    }

    @Test
    void deveUsarMensagemPadraoQuandoExcecaoNaoTemMensagem() {
        var resposta = handler.tratarNaoEncontrado(new EntityNotFoundException(), request);
        assertEquals("Not Found", resposta.getBody().get("mensagem"));
    }

    @Test
    void deveConverterViolacaoDeIntegridadeEm409() {
        var resposta = handler.tratarIntegridade(new DataIntegrityViolationException("constraint"), request);
        assertEquals(HttpStatus.CONFLICT, resposta.getStatusCode());
        assertTrue(resposta.getBody().get("mensagem").toString().contains("relacionado a outros dados"));
    }

    @Test
    void deveConverterJsonMalFormatadoEm400() {
        var resposta = handler.tratarFormatoInvalido(mock(HttpMessageNotReadableException.class), request);
        assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
        assertTrue(resposta.getBody().get("mensagem").toString().contains("mal formatados"));
    }

    @Test
    void deveConverterParametroDeTipoInvalidoEm400() {
        var excecao = mock(MethodArgumentTypeMismatchException.class);
        var resposta = handler.tratarFormatoInvalido(excecao, request);
        assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
    }
}
