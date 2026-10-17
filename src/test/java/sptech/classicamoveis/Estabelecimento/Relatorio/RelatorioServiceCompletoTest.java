package sptech.classicamoveis.Relatorio;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import sptech.classicamoveis.Cargo.model.Cargo;
import sptech.classicamoveis.Cliente.Cliente;
import sptech.classicamoveis.Colaborador.model.Colaborador;
import sptech.classicamoveis.Colaborador.repository.ColaboradorRepository;
import sptech.classicamoveis.Endereco.Endereco;
import sptech.classicamoveis.Estabelecimento.Estabelecimento;
import sptech.classicamoveis.Estabelecimento.repository.EstabelecimentoRepository;
import sptech.classicamoveis.Fornecedor.model.Fornecedor;
import sptech.classicamoveis.Fornecedor.repository.FornecedorRepository;
import sptech.classicamoveis.Movimentacao.ItemMovimentacao.ItemMovimentacao;
import sptech.classicamoveis.Movimentacao.ItemMovimentacao.ItemMovimentacaoRepository;
import sptech.classicamoveis.Movimentacao.Movimentacao;
import sptech.classicamoveis.Movimentacao.MovimentacaoRepository;
import sptech.classicamoveis.Movimentacao.Pagamento.FormaPagamento.FormaPagamento;
import sptech.classicamoveis.Movimentacao.Pagamento.Pagamento;
import sptech.classicamoveis.Movimentacao.Pagamento.repository.PagamentoRepository;
import sptech.classicamoveis.Movimentacao.StatusMovimentacao.StatusMovimentacao;
import sptech.classicamoveis.Movimentacao.TipoMovimentacao.TipoMovimentacao;
import sptech.classicamoveis.Movimentacao.service.EstoqueService;
import sptech.classicamoveis.Produto.model.Produto;
import sptech.classicamoveis.Produto.repository.ProdutoRepository;
import sptech.classicamoveis.Relatorio.dto.RelatorioTabelaPrecosDto;
import sptech.classicamoveis.Relatorio.dto.RelatorioVendaDto;
import sptech.classicamoveis.Relatorio.dto.RelatorioVendaPorVendedorDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/** Cobre os caminhos de sucesso e as ramificações do RelatorioService. */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RelatorioServiceCompletoTest {

    @Mock MovimentacaoRepository movimentacaoRepository;
    @Mock ItemMovimentacaoRepository itemRepository;
    @Mock PagamentoRepository pagamentoRepository;
    @Mock ColaboradorRepository colaboradorRepository;
    @Mock ProdutoRepository produtoRepository;
    @Mock EstoqueService estoqueService;
    @Mock EstabelecimentoRepository estabelecimentoRepository;
    @Mock FornecedorRepository fornecedorRepository;
    @InjectMocks RelatorioService service;

    // ---------- helpers ----------

    private Produto produto(int id, String sku, String nome, boolean ativo) {
        Produto p = new Produto();
        p.setId(id);
        p.setSku(sku);
        p.setNome(nome);
        p.setAtivo(ativo);
        p.setPrecoCusto(50.0);
        p.setPrecoVenda(100.0);
        return p;
    }

    private ItemMovimentacao item(Produto p, Integer qtd, Double preco, Double desconto) {
        ItemMovimentacao i = new ItemMovimentacao();
        i.setProduto(p);
        i.setQtd(qtd);
        i.setPrecoUnitario(preco);
        i.setDesconto(desconto);
        return i;
    }

    private Colaborador vendedor(Integer comissao, Cargo cargo) {
        Colaborador c = new Colaborador();
        c.setId(1);
        c.setNome("Ana");
        c.setSalario(3000.0);
        c.setComissao(comissao);
        c.setCargo(cargo);
        return c;
    }

    private Movimentacao venda(int id, Double valorTotal) {
        Movimentacao m = new Movimentacao();
        m.setId(id);
        m.setValorTotal(valorTotal);
        m.setDataHora(LocalDateTime.of(2026, 1, 10, 14, 0));
        return m;
    }

    // ---------- relatório por vendedor ----------

    @Test
    void vendedorComVendasCalculaTotaisEComissao() {
        Cargo cargo = new Cargo();
        cargo.setCargo("Vendedor");
        when(colaboradorRepository.findById(1)).thenReturn(Optional.of(vendedor(10, cargo)));
        Produto p = produto(3, "SKU3", "Sofá", true);
        Movimentacao v1 = venda(10, 200.0);
        Movimentacao v2 = venda(11, null);
        when(movimentacaoRepository.buscarVendasPorVendedor(eq(1), any(), any(),
                eq(TipoMovimentacao.VENDA), eq(StatusMovimentacao.CANCELADO))).thenReturn(List.of(v1, v2));
        when(itemRepository.findByMovimentacaoId(10)).thenReturn(List.of(
                item(p, 2, 100.0, 10.0),
                item(p, null, null, null)));
        when(itemRepository.findByMovimentacaoId(11)).thenReturn(List.of());

        RelatorioVendaPorVendedorDto dto = service.gerarRelatorioVendaPorVendedor(
                1, LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 31));

        assertEquals("Ana", dto.getVendedorNome());
        assertEquals("Vendedor", dto.getCargo());
        assertEquals(3000.0, dto.getSalario());
        assertEquals(10, dto.getComissao());
        assertEquals(2, dto.getVendas().size());
        assertEquals(200.0, dto.getTotalVendido());
        assertEquals(20.0, dto.getValorComissao());
        assertEquals(0.0, dto.getVendas().get(1).getValorTotalVenda());
        var itens = dto.getVendas().get(0).getItens();
        assertEquals(2, itens.size());
        assertEquals(190.0, itens.get(0).getTotalItem());
        assertEquals(100.0, itens.get(0).getValorUnitario());
        assertEquals(2, itens.get(0).getQuantidade());
        assertEquals("SKU3", itens.get(0).getSku());
        assertEquals(0.0, itens.get(1).getTotalItem());
        assertEquals(0, itens.get(1).getQuantidade());

        ArgumentCaptor<LocalDateTime> inicio = ArgumentCaptor.forClass(LocalDateTime.class);
        ArgumentCaptor<LocalDateTime> fim = ArgumentCaptor.forClass(LocalDateTime.class);
        verify(movimentacaoRepository).buscarVendasPorVendedor(eq(1), inicio.capture(), fim.capture(), any(), any());
        assertEquals(LocalDateTime.of(2026, 1, 1, 0, 0), inicio.getValue());
        assertEquals(LocalDateTime.of(2026, 2, 1, 0, 0), fim.getValue()); // fim é exclusivo (dia seguinte)
    }

    @Test
    void vendedorSemPeriodoUsaMesAtualAteHoje() {
        when(colaboradorRepository.findById(1)).thenReturn(Optional.of(vendedor(null, null)));
        when(movimentacaoRepository.buscarVendasPorVendedor(any(), any(), any(), any(), any())).thenReturn(List.of());

        RelatorioVendaPorVendedorDto dto = service.gerarRelatorioVendaPorVendedor(1, null, null);

        LocalDate hoje = LocalDate.now();
        assertEquals(hoje.withDayOfMonth(1), dto.getDataInicio());
        assertEquals(hoje, dto.getDataFim());
        assertNull(dto.getCargo());
        assertEquals(0.0, dto.getTotalVendido());
        assertEquals(0.0, dto.getValorComissao());
    }

    @Test
    void vendedorComSomenteDataFimUsaInicioDoMes() {
        when(colaboradorRepository.findById(1)).thenReturn(Optional.of(vendedor(5, null)));
        when(movimentacaoRepository.buscarVendasPorVendedor(any(), any(), any(), any(), any())).thenReturn(List.of());
        LocalDate fim = LocalDate.now();

        RelatorioVendaPorVendedorDto dto = service.gerarRelatorioVendaPorVendedor(1, null, fim);

        assertEquals(fim.withDayOfMonth(1), dto.getDataInicio());
        assertEquals(fim, dto.getDataFim());
    }

    @Test
    void vendedorComPeriodoDeUmUnicoDiaEhValido() {
        when(colaboradorRepository.findById(1)).thenReturn(Optional.of(vendedor(5, null)));
        when(movimentacaoRepository.buscarVendasPorVendedor(any(), any(), any(), any(), any())).thenReturn(List.of());
        LocalDate dia = LocalDate.of(2026, 3, 15);

        assertDoesNotThrow(() -> service.gerarRelatorioVendaPorVendedor(1, dia, dia));
    }

    // ---------- tabela de preços ----------

    @Test
    void tabelaDePrecosFornecedorInexistente() {
        when(estabelecimentoRepository.findById(1)).thenReturn(Optional.of(new Estabelecimento()));
        when(fornecedorRepository.findById(9L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.gerarTabelaPrecos(1, 9L));
    }

    @Test
    void tabelaDePrecosPorFornecedorFiltraInativosEOrdenaPorNome() {
        Estabelecimento loja = new Estabelecimento();
        loja.setNome("Loja Centro");
        Fornecedor f = new Fornecedor();
        f.setNome("Forn X");
        when(estabelecimentoRepository.findById(1)).thenReturn(Optional.of(loja));
        when(fornecedorRepository.findById(5L)).thenReturn(Optional.of(f));
        when(produtoRepository.findByFornecedorId(5L)).thenReturn(List.of(
                produto(1, "B", "banco", true),
                produto(2, "A", "Armário", true),
                produto(3, "I", "Inativo", false),
                produto(4, "N", null, true)));
        when(estoqueService.calcularSaldoProduto(1, 2)).thenReturn(5L);

        RelatorioTabelaPrecosDto dto = service.gerarTabelaPrecos(1, 5L);

        assertEquals("Loja Centro", dto.getEstabelecimento());
        assertEquals("Forn X", dto.getFornecedor());
        assertEquals(3, dto.getProdutos().size());
        assertNull(dto.getProdutos().get(0).getDescricao());      // nome nulo ordena primeiro
        assertEquals("Armário", dto.getProdutos().get(1).getDescricao());
        assertEquals("banco", dto.getProdutos().get(2).getDescricao());
        assertEquals(5L, dto.getProdutos().get(1).getQuantidadeEstoque());
        assertEquals("A", dto.getProdutos().get(1).getCodigoProduto());
        assertEquals(50.0, dto.getProdutos().get(1).getPrecoCompra());
        assertEquals(100.0, dto.getProdutos().get(1).getPrecoVenda());
        verify(produtoRepository, never()).findByAtivoTrue();
    }

    @Test
    void tabelaDePrecosSemFornecedorListaTodosOsAtivos() {
        Estabelecimento loja = new Estabelecimento();
        loja.setNome("Loja Centro");
        when(estabelecimentoRepository.findById(1)).thenReturn(Optional.of(loja));
        when(produtoRepository.findByAtivoTrue()).thenReturn(List.of(
                produto(1, "B", "banco", true),
                produto(2, "A", "armário", true),
                produto(3, "N", null, true)));

        RelatorioTabelaPrecosDto dto = service.gerarTabelaPrecos(1, null);

        assertEquals("Todos os fornecedores", dto.getFornecedor());
        assertEquals(3, dto.getProdutos().size());
        assertNull(dto.getProdutos().get(0).getDescricao());
        assertEquals("armário", dto.getProdutos().get(1).getDescricao());
        verify(fornecedorRepository, never()).findById(any());
        verify(produtoRepository, never()).findByFornecedorId(any());
    }

    // ---------- relatório de venda ----------

    private Cliente cliente(Endereco endereco) {
        Cliente c = new Cliente();
        c.setNome("Maria");
        c.setDocumento("12345678900");
        c.setTelefone1("11999999999");
        c.setEndereco(endereco);
        return c;
    }

    @Test
    void vendaCompletaMontaClienteVendedorProdutosEPagamentos() {
        Movimentacao m = venda(7, 205.0);
        m.setDataHoraEntrega(LocalDateTime.of(2026, 1, 20, 9, 0));
        m.setFrete(15.0);
        m.setCliente(cliente(new Endereco(1, "01001000", "Rua A", "Centro", "São Paulo", "10", null, "SP")));
        Colaborador col = new Colaborador();
        col.setNome("Ana");
        m.setColaborador(col);
        when(movimentacaoRepository.findById(7)).thenReturn(Optional.of(m));
        Produto p = produto(3, "SKU3", "Sofá", true);
        when(itemRepository.findByMovimentacaoId(7)).thenReturn(List.of(
                item(p, 2, 100.0, 10.0),
                item(p, null, null, null)));
        Pagamento pg = new Pagamento();
        pg.setFormaPagamento(FormaPagamento.PIX);
        pg.setValor(205.0);
        pg.setQuantidadeParcelas(1);
        when(pagamentoRepository.findByMovimentacaoId(7)).thenReturn(List.of(pg));

        RelatorioVendaDto dto = service.gerarRelatorioVenda(7);

        assertEquals(7, dto.getNumeroPedido());
        assertNotNull(dto.getDataHoraGeracao());
        assertEquals(LocalDateTime.of(2026, 1, 20, 9, 0), dto.getDataHoraEntrega());
        assertEquals("Maria", dto.getNomeCliente());
        assertEquals("12345678900", dto.getDocumentoCliente());
        assertEquals("11999999999", dto.getTelefoneCliente());
        String endereco = "Rua A, 10 - Centro - São Paulo/SP - CEP 01001000";
        assertEquals(endereco, dto.getEnderecoCompletoCliente());
        assertEquals(endereco, dto.getEnderecoEntrega());
        assertEquals("Ana", dto.getNomeVendedor());
        assertEquals(2, dto.getProdutos().size());
        assertEquals(190.0, dto.getProdutos().get(0).getTotal());
        assertEquals("SKU3", dto.getProdutos().get(0).getCodigoProduto());
        assertEquals(190.0, dto.getTotalProdutos());
        assertEquals(10.0, dto.getDesconto());
        assertEquals(15.0, dto.getFrete());
        assertEquals(205.0, dto.getTotalPedido());
        assertEquals(1, dto.getPagamentos().size());
        assertEquals("PIX", dto.getPagamentos().get(0).getFormaPagamento());
        assertEquals(205.0, dto.getPagamentos().get(0).getValor());
        assertEquals(1, dto.getPagamentos().get(0).getQuantidadeParcelas());
    }

    @Test
    void vendaSemClienteVendedorEFreteUsaValoresPadrao() {
        Movimentacao m = venda(8, 100.0);
        m.setFrete(null);
        when(movimentacaoRepository.findById(8)).thenReturn(Optional.of(m));
        when(itemRepository.findByMovimentacaoId(8)).thenReturn(List.of(item(produto(3, "S", "Sofá", true), 1, 100.0, null)));
        when(pagamentoRepository.findByMovimentacaoId(8)).thenReturn(List.of());

        RelatorioVendaDto dto = service.gerarRelatorioVenda(8);

        assertNull(dto.getNomeCliente());
        assertNull(dto.getNomeVendedor());
        assertEquals(0.0, dto.getFrete());
        assertEquals(100.0, dto.getTotalProdutos());
        assertEquals(100.0, dto.getTotalPedido());
        assertTrue(dto.getPagamentos().isEmpty());
    }

    @Test
    void enderecoDoClienteAusenteViraTraco() {
        Movimentacao m = venda(9, 0.0);
        m.setCliente(cliente(null));
        when(movimentacaoRepository.findById(9)).thenReturn(Optional.of(m));

        RelatorioVendaDto dto = service.gerarRelatorioVenda(9);

        assertEquals("-", dto.getEnderecoCompletoCliente());
        assertEquals("-", dto.getEnderecoEntrega());
    }

    @Test
    void enderecoSomenteComEstadoEhMontadoComBarra() {
        Movimentacao m = venda(9, 0.0);
        m.setCliente(cliente(new Endereco(1, null, null, null, null, null, null, "SP")));
        when(movimentacaoRepository.findById(9)).thenReturn(Optional.of(m));

        assertEquals("/SP", service.gerarRelatorioVenda(9).getEnderecoCompletoCliente());
    }

    @Test
    void enderecoSemLogradouroComecaPeloNumero() {
        Movimentacao m = venda(9, 0.0);
        m.setCliente(cliente(new Endereco(1, null, null, "Centro", null, "10", null, null)));
        when(movimentacaoRepository.findById(9)).thenReturn(Optional.of(m));

        assertEquals("10 - Centro", service.gerarRelatorioVenda(9).getEnderecoCompletoCliente());
    }

    @Test
    void enderecoSomenteComLogradouroECep() {
        Movimentacao m = venda(9, 0.0);
        m.setCliente(cliente(new Endereco(1, "01001000", "Rua A", null, null, null, null, null)));
        when(movimentacaoRepository.findById(9)).thenReturn(Optional.of(m));

        assertEquals("Rua A - CEP 01001000", service.gerarRelatorioVenda(9).getEnderecoCompletoCliente());
    }
}
