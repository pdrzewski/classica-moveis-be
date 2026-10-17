package sptech.classicamoveis.Movimentacao.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sptech.classicamoveis.Movimentacao.dto.*;
import sptech.classicamoveis.Movimentacao.service.EstoqueService;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EstoqueControllerTest {

    @Mock EstoqueService service;
    @InjectMocks EstoqueController controller;

    private InventarioProdutoDto inventarioDto() {
        return new InventarioProdutoDto(1, "Sofá", "Marca", "Fornecedor", "Sofás", 5L, 5);
    }

    @Test
    void buscarSaldo() {
        when(service.calcularSaldoProduto(1, 2)).thenReturn(7L);

        ResponseEntity<Map<String, Object>> r = controller.buscarSaldo(1, 2);

        assertEquals(HttpStatus.OK, r.getStatusCode());
        assertEquals(1, r.getBody().get("estabelecimentoId"));
        assertEquals(2, r.getBody().get("produtoId"));
        assertEquals(7L, r.getBody().get("saldo"));
    }

    @Test
    void buscarInventarioCompleto() {
        when(service.calcularInventarioCompleto(1)).thenReturn(Map.of(2, 3L));

        ResponseEntity<Map<String, Object>> r = controller.buscarInventarioCompleto(1);

        assertEquals(HttpStatus.OK, r.getStatusCode());
        assertEquals(1, r.getBody().get("estabelecimentoId"));
        assertEquals(Map.of(2, 3L), r.getBody().get("inventario"));
    }

    @Test
    void listarProdutosComSaldo() {
        when(service.listarProdutosComSaldo(null)).thenReturn(List.of(new ProdutoEstoqueResponseDto()));

        ResponseEntity<List<ProdutoEstoqueResponseDto>> r = controller.listarProdutosComSaldo(null);

        assertEquals(1, r.getBody().size());
    }

    @Test
    void buscarInventario() {
        when(service.buscarInventario(1)).thenReturn(List.of(inventarioDto()));

        assertEquals(1, controller.buscarInventario(1).getBody().size());
    }

    @Test
    void registrarContagem() {
        InventarioProdutoDto dto = inventarioDto();
        when(service.registrarContagem(1, 2, 5, 9)).thenReturn(dto);

        ResponseEntity<InventarioProdutoDto> r =
                controller.registrarContagem(1, 2, new InventarioContagemRequestDto(5, 9));

        assertEquals(HttpStatus.OK, r.getStatusCode());
        assertSame(dto, r.getBody());
    }

    @Test
    void registrarContagemLote() {
        List<InventarioContagemItemDto> itens = List.of(new InventarioContagemItemDto(2, 5));
        when(service.registrarContagemLote(1, 9, itens)).thenReturn(List.of(inventarioDto()));

        ResponseEntity<List<InventarioProdutoDto>> r =
                controller.registrarContagemLote(1, new InventarioContagemLoteRequestDto(9, itens));

        assertEquals(HttpStatus.OK, r.getStatusCode());
        assertEquals(1, r.getBody().size());
    }

    @Test
    void buscarExtratoMovimentacoes() {
        Map<String, Object> extrato = Map.of("saldoAtual", 3L);
        when(service.obterExtratoMovimentacoes(1, 2)).thenReturn(extrato);

        assertSame(extrato, controller.buscarExtratoMovimentacoes(1, 2).getBody());
    }
}
