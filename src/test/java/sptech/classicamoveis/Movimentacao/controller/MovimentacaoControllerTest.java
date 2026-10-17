package sptech.classicamoveis.Movimentacao.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sptech.classicamoveis.Movimentacao.TipoMovimentacao.TipoMovimentacao;
import sptech.classicamoveis.Movimentacao.dto.MovimentacaoRequestDto;
import sptech.classicamoveis.Movimentacao.dto.MovimentacaoResponseDto;
import sptech.classicamoveis.Movimentacao.service.MovimentacaoService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovimentacaoControllerTest {

    @Mock MovimentacaoService service;
    @InjectMocks MovimentacaoController controller;

    @Test
    void criarDelegaParaOService() {
        MovimentacaoRequestDto req = new MovimentacaoRequestDto();
        MovimentacaoResponseDto resp = new MovimentacaoResponseDto();
        when(service.criarMovimentacao(req)).thenReturn(resp);

        ResponseEntity<MovimentacaoResponseDto> r = controller.criar(req);

        assertEquals(HttpStatus.OK, r.getStatusCode());
        assertSame(resp, r.getBody());
    }

    @Test
    void buscarPorId() {
        MovimentacaoResponseDto resp = new MovimentacaoResponseDto();
        when(service.buscarPorId(1)).thenReturn(resp);

        ResponseEntity<MovimentacaoResponseDto> r = controller.buscarPorId(1);

        assertEquals(HttpStatus.OK, r.getStatusCode());
        assertSame(resp, r.getBody());
    }

    @Test
    void buscarVendasPendentes() {
        LocalDate inicio = LocalDate.of(2026, 1, 1);
        LocalDate fim = LocalDate.of(2026, 1, 31);
        when(service.buscarVendasPendentes(1, inicio, fim)).thenReturn(List.of(new MovimentacaoResponseDto()));

        ResponseEntity<List<MovimentacaoResponseDto>> r = controller.buscarVendasPendentes(1, inicio, fim);

        assertEquals(HttpStatus.OK, r.getStatusCode());
        assertEquals(1, r.getBody().size());
    }

    @Test
    void buscarVendasPendentesSemFiltros() {
        when(service.buscarVendasPendentes(null, null, null)).thenReturn(List.of());

        ResponseEntity<List<MovimentacaoResponseDto>> r = controller.buscarVendasPendentes(null, null, null);

        assertTrue(r.getBody().isEmpty());
    }

    @Test
    void concluir() {
        MovimentacaoResponseDto resp = new MovimentacaoResponseDto();
        when(service.concluir(3)).thenReturn(resp);

        assertSame(resp, controller.concluir(3).getBody());
        verify(service).concluir(3);
    }

    @Test
    void cancelar() {
        MovimentacaoResponseDto resp = new MovimentacaoResponseDto();
        when(service.cancelar(4)).thenReturn(resp);

        assertSame(resp, controller.cancelar(4).getBody());
        verify(service).cancelar(4);
    }

    @Test
    void ocultar() {
        MovimentacaoResponseDto resp = new MovimentacaoResponseDto();
        when(service.ocultar(5)).thenReturn(resp);

        assertSame(resp, controller.ocultar(5).getBody());
        verify(service).ocultar(5);
    }

    @Test
    void buscarHistorico() {
        Page<MovimentacaoResponseDto> pagina = new PageImpl<>(List.of(new MovimentacaoResponseDto()));
        when(service.buscarHistorico(TipoMovimentacao.VENDA, 1, 0, 10)).thenReturn(pagina);

        ResponseEntity<Page<MovimentacaoResponseDto>> r = controller.buscarHistorico(TipoMovimentacao.VENDA, 1, 0, 10);

        assertEquals(HttpStatus.OK, r.getStatusCode());
        assertEquals(1, r.getBody().getTotalElements());
    }
}
