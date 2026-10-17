package sptech.classicamoveis.Colaborador;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sptech.classicamoveis.Colaborador.controller.ColaboradorController;
import sptech.classicamoveis.Colaborador.dto.ColaboradorRequestDto;
import sptech.classicamoveis.Colaborador.dto.ColaboradorResponseDto;
import sptech.classicamoveis.Colaborador.service.ColaboradorService;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/** Cobre os endpoints do ColaboradorController que o ColaboradorControllerTest não exercita. */
@ExtendWith(MockitoExtension.class)
class ColaboradorControllerComplementoTest {

    @Mock ColaboradorService service;
    @InjectMocks ColaboradorController controller;

    @Test
    void criarRetorna201() {
        ColaboradorRequestDto req = mock(ColaboradorRequestDto.class);
        ColaboradorResponseDto resp = new ColaboradorResponseDto();
        when(service.criar(req)).thenReturn(resp);

        ResponseEntity<ColaboradorResponseDto> r = controller.criar(req);

        assertEquals(HttpStatus.CREATED, r.getStatusCode());
        assertSame(resp, r.getBody());
    }

    @Test
    void atualizarRetorna200() {
        ColaboradorRequestDto req = mock(ColaboradorRequestDto.class);
        ColaboradorResponseDto resp = new ColaboradorResponseDto();
        when(service.atualizar(1, req)).thenReturn(resp);

        ResponseEntity<ColaboradorResponseDto> r = controller.atualizar(1, req);

        assertEquals(HttpStatus.OK, r.getStatusCode());
        assertSame(resp, r.getBody());
    }

    @Test
    void deletarRetorna204() {
        ResponseEntity<Void> r = controller.deletar(1);

        assertEquals(HttpStatus.NO_CONTENT, r.getStatusCode());
        verify(service).deletar(1);
    }

    @Test
    void registrarFerias() {
        FeriasRequestDto req = mock(FeriasRequestDto.class);
        ColaboradorResponseDto resp = new ColaboradorResponseDto();
        when(service.registrarFerias(1, req)).thenReturn(resp);

        assertSame(resp, controller.registrarFerias(1, req).getBody());
    }

    @Test
    void encerrarFerias() {
        ColaboradorResponseDto resp = new ColaboradorResponseDto();
        when(service.encerrarFerias(1)).thenReturn(resp);

        assertSame(resp, controller.encerrarFerias(1).getBody());
    }

    @Test
    void listarEmFerias() {
        when(service.listarEmFerias()).thenReturn(List.of(new ColaboradorResponseDto()));

        assertEquals(1, controller.listarEmFerias().getBody().size());
    }

    @Test
    void tratamentoDeIllegalArgumentRetorna400ComMensagem() {
        ResponseEntity<Map<String, String>> r =
                controller.handleIllegalArgumentException(new IllegalArgumentException("inválido"));

        assertEquals(HttpStatus.BAD_REQUEST, r.getStatusCode());
        assertEquals("inválido", r.getBody().get("mensagem"));
    }

    @Test
    void tratamentoDeDuplicidadeRetorna409() {
        ResponseEntity<Map<String, String>> r =
                controller.handleDataIntegrityViolationException(new DataIntegrityViolationException("dup"));

        assertEquals(HttpStatus.CONFLICT, r.getStatusCode());
        assertTrue(r.getBody().get("mensagem").contains("duplicado"));
    }
}
