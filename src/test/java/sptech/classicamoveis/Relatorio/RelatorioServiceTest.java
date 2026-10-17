package sptech.classicamoveis.Relatorio;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sptech.classicamoveis.Colaborador.model.Colaborador;
import sptech.classicamoveis.Colaborador.repository.ColaboradorRepository;
import sptech.classicamoveis.Estabelecimento.Estabelecimento;
import sptech.classicamoveis.Estabelecimento.repository.EstabelecimentoRepository;
import sptech.classicamoveis.Fornecedor.repository.FornecedorRepository;
import sptech.classicamoveis.Movimentacao.ItemMovimentacao.ItemMovimentacaoRepository;
import sptech.classicamoveis.Movimentacao.MovimentacaoRepository;
import sptech.classicamoveis.Movimentacao.Pagamento.repository.PagamentoRepository;
import sptech.classicamoveis.Movimentacao.service.EstoqueService;
import sptech.classicamoveis.Produto.repository.ProdutoRepository;
import sptech.classicamoveis.Relatorio.dto.RelatorioTabelaPrecosDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RelatorioServiceTest {
    @Mock MovimentacaoRepository movimentacaoRepository;
    @Mock ItemMovimentacaoRepository itemRepository;
    @Mock PagamentoRepository pagamentoRepository;
    @Mock ColaboradorRepository colaboradorRepository;
    @Mock ProdutoRepository produtoRepository;
    @Mock EstoqueService estoqueService;
    @Mock EstabelecimentoRepository estabelecimentoRepository;
    @Mock FornecedorRepository fornecedorRepository;
    @InjectMocks RelatorioService service;

    @Test void vendedorInexistente() {
        when(colaboradorRepository.findById(99)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.gerarRelatorioVendaPorVendedor(99, null, null));
    }

    @Test void rejeitaPeriodoComDataFimAnterior() {
        Colaborador vendedor = new Colaborador();
        vendedor.setId(3);
        when(colaboradorRepository.findById(3)).thenReturn(Optional.of(vendedor));
        assertThrows(IllegalArgumentException.class, () -> service.gerarRelatorioVendaPorVendedor(
                3, LocalDate.of(2026, 10, 10), LocalDate.of(2026, 10, 1)));
        verifyNoInteractions(movimentacaoRepository);
    }

    @Test void geraRelatorioVendedorSemVendas() {
        Colaborador vendedor = new Colaborador();
        vendedor.setId(3); vendedor.setNome("Ana"); vendedor.setSalario(2500.0); vendedor.setComissao(2);
        when(colaboradorRepository.findById(3)).thenReturn(Optional.of(vendedor));
        when(movimentacaoRepository.buscarVendasPorVendedor(eq(3), any(), any(), any(), any())).thenReturn(List.of());

        var resultado = service.gerarRelatorioVendaPorVendedor(3, LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 15));

        assertEquals("Ana", resultado.getVendedorNome());
        assertEquals(0.0, resultado.getTotalVendido());
        assertEquals(0.0, resultado.getValorComissao());
        assertTrue(resultado.getVendas().isEmpty());
    }

    @Test void vendaInexistente() {
        when(movimentacaoRepository.findById(99)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.gerarRelatorioVenda(99));
    }

    @Test void estabelecimentoInexistenteNaTabelaDePrecos() {
        when(estabelecimentoRepository.findById(7)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.gerarTabelaPrecos(7, null));
    }

    @Test void geraTabelaDePrecosSemProdutos() {
        Estabelecimento loja = new Estabelecimento();
        loja.setNome("Matriz");
        when(estabelecimentoRepository.findById(1)).thenReturn(Optional.of(loja));
        when(produtoRepository.findByAtivoTrue()).thenReturn(List.of());

        RelatorioTabelaPrecosDto resultado = service.gerarTabelaPrecos(1, null);

        assertEquals("Matriz", resultado.getEstabelecimento());
        assertEquals("Todos os fornecedores", resultado.getFornecedor());
        assertTrue(resultado.getProdutos().isEmpty());
    }
}
