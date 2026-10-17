package sptech.classicamoveis.Movimentacao.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import sptech.classicamoveis.Categoria.Categoria;
import sptech.classicamoveis.Colaborador.model.Colaborador;
import sptech.classicamoveis.Colaborador.repository.ColaboradorRepository;
import sptech.classicamoveis.Estabelecimento.Estabelecimento;
import sptech.classicamoveis.Estabelecimento.repository.EstabelecimentoRepository;
import sptech.classicamoveis.Fornecedor.model.Fornecedor;
import sptech.classicamoveis.Movimentacao.ItemMovimentacao.ItemMovimentacao;
import sptech.classicamoveis.Movimentacao.ItemMovimentacao.ItemMovimentacaoRepository;
import sptech.classicamoveis.Movimentacao.Movimentacao;
import sptech.classicamoveis.Movimentacao.MovimentacaoRepository;
import sptech.classicamoveis.Movimentacao.StatusMovimentacao.StatusMovimentacao;
import sptech.classicamoveis.Movimentacao.TipoMovimentacao.TipoMovimentacao;
import sptech.classicamoveis.Movimentacao.dto.InventarioContagemItemDto;
import sptech.classicamoveis.Movimentacao.dto.InventarioProdutoDto;
import sptech.classicamoveis.Movimentacao.dto.ProdutoEstoqueResponseDto;
import sptech.classicamoveis.Produto.mapper.ProdutoMapper;
import sptech.classicamoveis.Produto.model.Produto;
import sptech.classicamoveis.Produto.repository.ProdutoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class EstoqueServiceTest {

    @Mock MovimentacaoRepository movRepo;
    @Mock ItemMovimentacaoRepository itemRepo;
    @Mock ProdutoRepository produtoRepo;
    @Mock ProdutoMapper produtoMapper;
    @Mock ColaboradorRepository colaboradorRepo;
    @Mock EstabelecimentoRepository estabelecimentoRepo;
    @InjectMocks EstoqueService service;

    // ---------- helpers ----------

    private Movimentacao mov(int id, TipoMovimentacao tipo, StatusMovimentacao status) {
        Movimentacao m = new Movimentacao();
        m.setId(id);
        m.setTipoMovimentacao(tipo);
        m.setStatus(status);
        return m;
    }

    private Produto produto(int id, boolean ativo) {
        Produto p = new Produto();
        p.setId(id);
        p.setNome("Produto " + id);
        p.setMarca("Marca");
        p.setSku("SKU" + id);
        p.setEstoqueMinimo(2);
        p.setAtivo(ativo);
        Fornecedor f = new Fornecedor();
        f.setNome("Fornecedor X");
        p.setFornecedor(f);
        Categoria c = new Categoria();
        c.setCategoria("Sofás");
        p.setCategoria(c);
        return p;
    }

    private ItemMovimentacao item(Movimentacao m, Produto p, int qtd) {
        ItemMovimentacao i = new ItemMovimentacao();
        i.setMovimentacao(m);
        i.setProduto(p);
        i.setQtd(qtd);
        return i;
    }

    // ---------- saldo ----------

    @Test
    void saldoSemMovimentacoesEhZero() {
        assertEquals(0.0, service.calcularSaldoDisponivel(1, 1));
        assertEquals(0L, service.calcularSaldoProduto(1, 1));
    }

    @Test
    void saldoSomaEntradasESubtraiSaidas() {
        Produto p1 = produto(1, true);
        Produto p2 = produto(2, true);

        Movimentacao compra = mov(1, TipoMovimentacao.COMPRA, StatusMovimentacao.CONCLUIDO);
        Movimentacao transfIn = mov(2, TipoMovimentacao.TRANSFERENCIA, StatusMovimentacao.CONCLUIDO);
        Movimentacao ajusteIn = mov(3, TipoMovimentacao.AJUSTE_ENTRADA, StatusMovimentacao.CONCLUIDO);
        Movimentacao vendaPend = mov(4, TipoMovimentacao.VENDA, StatusMovimentacao.PENDENTE);
        Movimentacao vendaConc = mov(5, TipoMovimentacao.VENDA, StatusMovimentacao.CONCLUIDO);
        Movimentacao transfOut = mov(6, TipoMovimentacao.TRANSFERENCIA, StatusMovimentacao.CONCLUIDO);
        Movimentacao ajusteOut = mov(7, TipoMovimentacao.AJUSTE_SAIDA, StatusMovimentacao.CONCLUIDO);

        when(movRepo.findByTipoMovimentacaoAndStatusAndEstabelecimentoDestinoId(
                TipoMovimentacao.COMPRA, StatusMovimentacao.CONCLUIDO, 1)).thenReturn(List.of(compra));
        when(movRepo.findByTipoMovimentacaoAndStatusAndEstabelecimentoDestinoId(
                TipoMovimentacao.TRANSFERENCIA, StatusMovimentacao.CONCLUIDO, 1)).thenReturn(List.of(transfIn));
        when(movRepo.findByTipoMovimentacaoAndStatusAndEstabelecimentoOrigemId(
                TipoMovimentacao.AJUSTE_ENTRADA, StatusMovimentacao.CONCLUIDO, 1)).thenReturn(List.of(ajusteIn));
        when(movRepo.findByTipoMovimentacaoAndStatusAndEstabelecimentoOrigemId(
                TipoMovimentacao.VENDA, StatusMovimentacao.PENDENTE, 1)).thenReturn(List.of(vendaPend));
        when(movRepo.findByTipoMovimentacaoAndStatusAndEstabelecimentoOrigemId(
                TipoMovimentacao.VENDA, StatusMovimentacao.CONCLUIDO, 1)).thenReturn(List.of(vendaConc));
        when(movRepo.findByTipoMovimentacaoAndStatusAndEstabelecimentoOrigemId(
                TipoMovimentacao.TRANSFERENCIA, StatusMovimentacao.CONCLUIDO, 1)).thenReturn(List.of(transfOut));
        when(movRepo.findByTipoMovimentacaoAndStatusAndEstabelecimentoOrigemId(
                TipoMovimentacao.AJUSTE_SAIDA, StatusMovimentacao.CONCLUIDO, 1)).thenReturn(List.of(ajusteOut));

        // entradas do produto 1: 10 + 5 + 3 = 18 (o item do produto 2 deve ser ignorado)
        when(itemRepo.findByMovimentacaoId(1)).thenReturn(List.of(item(compra, p1, 10), item(compra, p2, 100)));
        when(itemRepo.findByMovimentacaoId(2)).thenReturn(List.of(item(transfIn, p1, 5)));
        when(itemRepo.findByMovimentacaoId(3)).thenReturn(List.of(item(ajusteIn, p1, 3)));
        // saídas do produto 1: 2 (pendente) + 4 (concluída) + 1 + 1 = 8
        when(itemRepo.findByMovimentacaoId(4)).thenReturn(List.of(item(vendaPend, p1, 2), item(vendaPend, p2, 50)));
        when(itemRepo.findByMovimentacaoId(5)).thenReturn(List.of(item(vendaConc, p1, 4)));
        when(itemRepo.findByMovimentacaoId(6)).thenReturn(List.of(item(transfOut, p1, 1), item(transfOut, p2, 9)));
        when(itemRepo.findByMovimentacaoId(7)).thenReturn(List.of(item(ajusteOut, p1, 1)));

        assertEquals(10.0, service.calcularSaldoDisponivel(1, 1));
        assertEquals(10L, service.calcularSaldoProduto(1, 1));
        // produto 2 também tem movimentos: 100 de entrada contra 50 + 9 de saída
        assertEquals(41.0, service.calcularSaldoDisponivel(2, 1));
    }

    // ---------- extrato ----------

    @Test
    void extratoIgnoraNaoConcluidasEDuplicadas() {
        Produto p = produto(1, true);
        Movimentacao concluida = mov(1, TipoMovimentacao.COMPRA, StatusMovimentacao.CONCLUIDO);
        Movimentacao pendente = mov(2, TipoMovimentacao.VENDA, StatusMovimentacao.PENDENTE);
        Movimentacao outra = mov(3, TipoMovimentacao.AJUSTE_ENTRADA, StatusMovimentacao.CONCLUIDO);

        when(itemRepo.findByProdutoIdAndEstabelecimentoId(1, 1)).thenReturn(List.of(
                item(concluida, p, 1), item(concluida, p, 2), item(pendente, p, 3), item(outra, p, 4)));

        List<Movimentacao> extrato = service.obterExtrato(1, 1);

        assertEquals(2, extrato.size());
        assertEquals(1, extrato.get(0).getId());
        assertEquals(3, extrato.get(1).getId());
    }

    @Test
    void extratoVazio() {
        when(itemRepo.findByProdutoIdAndEstabelecimentoId(1, 1)).thenReturn(List.of());
        assertTrue(service.obterExtrato(1, 1).isEmpty());
    }

    @Test
    void extratoMovimentacoesMontaMapaComQuantidadeDoProduto() {
        Produto p1 = produto(1, true);
        Produto p2 = produto(2, true);
        Movimentacao m = mov(1, TipoMovimentacao.COMPRA, StatusMovimentacao.CONCLUIDO);
        m.setObservacao("obs");

        when(itemRepo.findByProdutoIdAndEstabelecimentoId(1, 1)).thenReturn(List.of(item(m, p1, 5)));
        when(itemRepo.findByMovimentacaoId(1)).thenReturn(List.of(item(m, p1, 5), item(m, p2, 9)));

        Map<String, Object> extrato = service.obterExtratoMovimentacoes(1, 1);

        assertEquals(1, extrato.get("estabelecimentoId"));
        assertEquals(1, extrato.get("produtoId"));
        assertEquals(0L, extrato.get("saldoAtual"));
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> lista = (List<Map<String, Object>>) extrato.get("movimentacoes");
        assertEquals(1, lista.size());
        assertEquals(5.0, lista.get(0).get("quantidade"));
        assertEquals("obs", lista.get(0).get("observacao"));
        assertEquals(TipoMovimentacao.COMPRA, lista.get(0).get("tipo"));
        assertEquals(StatusMovimentacao.CONCLUIDO, lista.get(0).get("status"));
    }

    // ---------- inventário completo ----------

    @Test
    void inventarioCompletoAgrupaProdutosDasMovimentacoes() {
        Produto p1 = produto(1, true);
        Produto p2 = produto(2, true);
        Movimentacao origem = mov(20, TipoMovimentacao.VENDA, StatusMovimentacao.CONCLUIDO);
        Movimentacao destino = mov(21, TipoMovimentacao.TRANSFERENCIA, StatusMovimentacao.CONCLUIDO);
        Movimentacao compra = mov(30, TipoMovimentacao.COMPRA, StatusMovimentacao.CONCLUIDO);

        // o service chama addAll na primeira lista, então ela precisa ser mutável
        when(movRepo.findByEstabelecimentoOrigemId(1)).thenReturn(new ArrayList<>(List.of(origem)));
        when(movRepo.findByEstabelecimentoDestinoId(1)).thenReturn(List.of(destino));
        when(movRepo.findByTipoMovimentacaoAndStatusAndEstabelecimentoDestinoId(
                TipoMovimentacao.COMPRA, StatusMovimentacao.CONCLUIDO, 1)).thenReturn(List.of(compra));

        when(itemRepo.findByMovimentacaoId(20)).thenReturn(List.of(item(origem, p1, 1), item(origem, p1, 1)));
        when(itemRepo.findByMovimentacaoId(21)).thenReturn(List.of(item(destino, p2, 1)));
        when(itemRepo.findByMovimentacaoId(30)).thenReturn(List.of(item(compra, p1, 7)));

        Map<Integer, Long> inventario = service.calcularInventarioCompleto(1);

        assertEquals(2, inventario.size());
        assertEquals(7L, inventario.get(1));
        assertEquals(0L, inventario.get(2));
    }

    @Test
    void inventarioCompletoVazio() {
        when(movRepo.findByEstabelecimentoOrigemId(1)).thenReturn(new ArrayList<>());
        when(movRepo.findByEstabelecimentoDestinoId(1)).thenReturn(List.of());
        assertTrue(service.calcularInventarioCompleto(1).isEmpty());
    }

    // ---------- saldo geral / listagem ----------

    @Test
    void saldoGeralConsideraApenasConcluidasEOProdutoPedido() {
        Produto p1 = produto(1, true);
        Produto p9 = produto(9, true);

        Movimentacao compra = mov(1, TipoMovimentacao.COMPRA, StatusMovimentacao.CONCLUIDO);
        Movimentacao transfEntrada = mov(2, TipoMovimentacao.TRANSFERENCIA, StatusMovimentacao.CONCLUIDO);
        transfEntrada.setEstabelecimentoDestino(new Estabelecimento());
        Movimentacao venda = mov(3, TipoMovimentacao.VENDA, StatusMovimentacao.CONCLUIDO);
        Movimentacao vendaPendente = mov(4, TipoMovimentacao.VENDA, StatusMovimentacao.PENDENTE);
        Movimentacao ajusteIn = mov(5, TipoMovimentacao.AJUSTE_ENTRADA, StatusMovimentacao.CONCLUIDO);
        Movimentacao ajusteOut = mov(6, TipoMovimentacao.AJUSTE_SAIDA, StatusMovimentacao.CONCLUIDO);
        Movimentacao transfSaida = mov(7, TipoMovimentacao.TRANSFERENCIA, StatusMovimentacao.CONCLUIDO);
        transfSaida.setEstabelecimentoOrigem(new Estabelecimento());

        when(movRepo.findAll()).thenReturn(List.of(compra, transfEntrada, venda, vendaPendente, ajusteIn, ajusteOut, transfSaida));
        when(itemRepo.findByMovimentacaoId(1)).thenReturn(List.of(item(compra, p1, 10), item(compra, p9, 50)));
        when(itemRepo.findByMovimentacaoId(2)).thenReturn(List.of(item(transfEntrada, p1, 3)));
        when(itemRepo.findByMovimentacaoId(3)).thenReturn(List.of(item(venda, p1, 4)));
        when(itemRepo.findByMovimentacaoId(5)).thenReturn(List.of(item(ajusteIn, p1, 2)));
        when(itemRepo.findByMovimentacaoId(6)).thenReturn(List.of(item(ajusteOut, p1, 1)));
        when(itemRepo.findByMovimentacaoId(7)).thenReturn(List.of(item(transfSaida, p1, 1)));

        // entradas 10 + 3 + 2 = 15; saídas 4 + 1 + 1 = 6
        assertEquals(9L, service.calcularSaldoGeralProduto(1));
        assertEquals(50L, service.calcularSaldoGeralProduto(9));
        verify(itemRepo, never()).findByMovimentacaoId(4); // venda pendente é descartada antes
    }

    @Test
    void listarProdutosSemEstabelecimentoUsaSaldoGeralEPulaInativos() {
        Produto ativo = produto(1, true);
        Produto inativo = produto(2, false);
        Produto semFlag = produto(3, true);
        semFlag.setAtivo(null);

        when(produtoRepo.findAll()).thenReturn(List.of(ativo, inativo, semFlag));
        when(movRepo.findAll()).thenReturn(List.of());

        List<ProdutoEstoqueResponseDto> resposta = service.listarProdutosComSaldo(null);

        assertEquals(2, resposta.size());
        assertEquals(1, resposta.get(0).getProdutoId());
        assertEquals(3, resposta.get(1).getProdutoId());
        assertEquals(0L, resposta.get(0).getSaldoDisponivel());
        assertEquals("Fornecedor X", resposta.get(0).getFornecedor());
        assertEquals("SKU1", resposta.get(0).getSku());
        assertEquals(2, resposta.get(0).getEstoqueMinimo());
    }

    @Test
    void listarProdutosComEstabelecimentoUsaSaldoDaLoja() {
        Produto p = produto(1, true);
        when(produtoRepo.findAll()).thenReturn(List.of(p));

        List<ProdutoEstoqueResponseDto> resposta = service.listarProdutosComSaldo(1);

        assertEquals(1, resposta.size());
        assertEquals(0L, resposta.get(0).getSaldoDisponivel());
        verify(movRepo, never()).findAll();
    }

    // ---------- buscarInventario ----------

    @Test
    void buscarInventarioEstabelecimentoInexistente() {
        when(estabelecimentoRepo.existsById(1)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> service.buscarInventario(1));
    }

    @Test
    void buscarInventarioMontaDtoPorProdutoAtivo() {
        when(estabelecimentoRepo.existsById(1)).thenReturn(true);
        when(produtoRepo.findByAtivoTrue()).thenReturn(List.of(produto(1, true), produto(2, true)));

        List<InventarioProdutoDto> inventario = service.buscarInventario(1);

        assertEquals(2, inventario.size());
        InventarioProdutoDto dto = inventario.get(0);
        assertEquals(1, dto.produtoId());
        assertEquals("Produto 1", dto.produto());
        assertEquals("Marca", dto.marca());
        assertEquals("Fornecedor X", dto.fornecedora());
        assertEquals("Sofás", dto.categoria());
        assertEquals(0L, dto.quantidadeAtual());
        assertNull(dto.quantidadeContada());
    }

    // ---------- registrarContagem ----------

    private void prepararContagem(Produto p) {
        when(estabelecimentoRepo.existsById(1)).thenReturn(true);
        when(produtoRepo.findById(p.getId())).thenReturn(Optional.of(p));
        when(colaboradorRepo.findById(5)).thenReturn(Optional.of(new Colaborador()));
    }

    @Test
    void contagemRejeitaQuantidadeNula() {
        assertThrows(IllegalArgumentException.class, () -> service.registrarContagem(1, 1, null, 5));
    }

    @Test
    void contagemRejeitaQuantidadeNegativa() {
        assertThrows(IllegalArgumentException.class, () -> service.registrarContagem(1, 1, -1, 5));
    }

    @Test
    void contagemEstabelecimentoInexistente() {
        when(estabelecimentoRepo.existsById(1)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> service.registrarContagem(1, 1, 3, 5));
    }

    @Test
    void contagemProdutoInexistente() {
        when(estabelecimentoRepo.existsById(1)).thenReturn(true);
        when(produtoRepo.findById(1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.registrarContagem(1, 1, 3, 5));
    }

    @Test
    void contagemProdutoInativo() {
        when(estabelecimentoRepo.existsById(1)).thenReturn(true);
        when(produtoRepo.findById(1)).thenReturn(Optional.of(produto(1, false)));
        assertThrows(IllegalArgumentException.class, () -> service.registrarContagem(1, 1, 3, 5));
    }

    @Test
    void contagemColaboradorInexistente() {
        when(estabelecimentoRepo.existsById(1)).thenReturn(true);
        when(produtoRepo.findById(1)).thenReturn(Optional.of(produto(1, true)));
        when(colaboradorRepo.findById(5)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.registrarContagem(1, 1, 3, 5));
    }

    @Test
    void contagemIgualAoSaldoNaoGeraAjuste() {
        prepararContagem(produto(1, true));

        InventarioProdutoDto dto = service.registrarContagem(1, 1, 0, 5);

        assertEquals(0L, dto.quantidadeAtual());
        assertEquals(0, dto.quantidadeContada());
        verify(movRepo, never()).save(any());
        verify(itemRepo, never()).save(any());
    }

    @Test
    void contagemMaiorQueSaldoGeraAjusteDeEntrada() {
        prepararContagem(produto(1, true));
        Estabelecimento loja = new Estabelecimento();
        loja.setId(1);
        when(estabelecimentoRepo.findById(1)).thenReturn(Optional.of(loja));
        when(movRepo.save(any(Movimentacao.class))).thenAnswer(inv -> inv.getArgument(0));

        service.registrarContagem(1, 1, 5, 5);

        ArgumentCaptor<Movimentacao> movCaptor = ArgumentCaptor.forClass(Movimentacao.class);
        verify(movRepo).save(movCaptor.capture());
        Movimentacao salva = movCaptor.getValue();
        assertEquals(TipoMovimentacao.AJUSTE_ENTRADA, salva.getTipoMovimentacao());
        assertEquals(StatusMovimentacao.CONCLUIDO, salva.getStatus());
        assertSame(loja, salva.getEstabelecimentoOrigem());
        assertEquals(0.0, salva.getValorTotal());
        assertTrue(salva.getObservacao().contains("Diferença: 5"));

        ArgumentCaptor<ItemMovimentacao> itemCaptor = ArgumentCaptor.forClass(ItemMovimentacao.class);
        verify(itemRepo).save(itemCaptor.capture());
        assertEquals(5, itemCaptor.getValue().getQtd());
        assertEquals(0.0, itemCaptor.getValue().getPrecoUnitario());
        assertEquals(0.0, itemCaptor.getValue().getDesconto());
    }

    @Test
    void contagemMenorQueSaldoGeraAjusteDeSaida() {
        Produto p = produto(1, true);
        prepararContagem(p);
        Movimentacao compra = mov(1, TipoMovimentacao.COMPRA, StatusMovimentacao.CONCLUIDO);
        when(movRepo.findByTipoMovimentacaoAndStatusAndEstabelecimentoDestinoId(
                TipoMovimentacao.COMPRA, StatusMovimentacao.CONCLUIDO, 1)).thenReturn(List.of(compra));
        when(itemRepo.findByMovimentacaoId(1)).thenReturn(List.of(item(compra, p, 10)));
        when(estabelecimentoRepo.findById(1)).thenReturn(Optional.of(new Estabelecimento()));
        when(movRepo.save(any(Movimentacao.class))).thenAnswer(inv -> inv.getArgument(0));

        service.registrarContagem(1, 1, 4, 5);

        ArgumentCaptor<Movimentacao> movCaptor = ArgumentCaptor.forClass(Movimentacao.class);
        verify(movRepo).save(movCaptor.capture());
        assertEquals(TipoMovimentacao.AJUSTE_SAIDA, movCaptor.getValue().getTipoMovimentacao());

        ArgumentCaptor<ItemMovimentacao> itemCaptor = ArgumentCaptor.forClass(ItemMovimentacao.class);
        verify(itemRepo).save(itemCaptor.capture());
        assertEquals(6, itemCaptor.getValue().getQtd());
    }

    @Test
    void contagemFalhaQuandoEstabelecimentoSomeNaCriacaoDoAjuste() {
        prepararContagem(produto(1, true));
        when(estabelecimentoRepo.findById(1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.registrarContagem(1, 1, 5, 5));
    }

    // ---------- registrarContagemLote ----------

    @Test
    void contagemEmLoteEstabelecimentoInexistente() {
        when(estabelecimentoRepo.existsById(1)).thenReturn(false);
        assertThrows(EntityNotFoundException.class,
                () -> service.registrarContagemLote(1, 5, List.of(new InventarioContagemItemDto(1, 0))));
    }

    @Test
    void contagemEmLoteProcessaTodosOsItens() {
        when(estabelecimentoRepo.existsById(1)).thenReturn(true);
        when(produtoRepo.findById(1)).thenReturn(Optional.of(produto(1, true)));
        when(produtoRepo.findById(2)).thenReturn(Optional.of(produto(2, true)));
        when(colaboradorRepo.findById(5)).thenReturn(Optional.of(new Colaborador()));

        List<InventarioProdutoDto> resultado = service.registrarContagemLote(1, 5, List.of(
                new InventarioContagemItemDto(1, 0),
                new InventarioContagemItemDto(2, 0)));

        assertEquals(2, resultado.size());
        assertEquals(1, resultado.get(0).produtoId());
        assertEquals(2, resultado.get(1).produtoId());
    }
}
