package sptech.classicamoveis.Relatorio;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sptech.classicamoveis.Movimentacao.ItemMovimentacao.ItemMovimentacaoRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RelatorioServiceTest {

 @Mock
 ItemMovimentacaoRepository repository;

 @InjectMocks
 RelatorioService service;

 @Test
 void fornecedor() {
  List<RelatorioVendaItemDto> x = List.of();

  when(repository.relatorioVendasPorFornecedor(1, 2))
          .thenReturn(x);

  assertSame(
          x,
          service.relatorioVendasPorFornecedor(1, 2)
  );
 }

 @Test
 void produtoSemFiltro() {
  List<RelatorioVendasPorProdutoDto> x = List.of();

  when(repository.relatorioVendasPorProduto(
          1,
          2,
          null,
          null
  )).thenReturn(x);

  assertSame(
          x,
          service.relatorioVendasPorProduto(
                  1,
                  2,
                  null,
                  null,
                  null
          )
  );
 }

 @Test
 void produtoComFiltro() {
  var a = mock(RelatorioVendasPorProdutoDto.class);
  var b = mock(RelatorioVendasPorProdutoDto.class);

  when(a.produtoId()).thenReturn(1);
  when(b.produtoId()).thenReturn(2);

  when(repository.relatorioVendasPorProduto(
          any(),
          any(),
          any(),
          any()
  )).thenReturn(List.of(a, b));

  LocalDateTime inicio = LocalDateTime.of(2026, 1, 1, 0, 0);
  LocalDateTime fim = LocalDateTime.of(2026, 1, 31, 23, 59);

  assertEquals(
          List.of(a),
          service.relatorioVendasPorProduto(
                  1,
                  2,
                  List.of(1),
                  inicio,
                  fim
          )
  );
 }
}
