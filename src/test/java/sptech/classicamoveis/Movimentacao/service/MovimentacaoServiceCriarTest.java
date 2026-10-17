package sptech.classicamoveis.Movimentacao.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import sptech.classicamoveis.Cliente.Cliente;
import sptech.classicamoveis.Cliente.repository.ClienteRepository;
import sptech.classicamoveis.Colaborador.model.Colaborador;
import sptech.classicamoveis.Colaborador.repository.ColaboradorRepository;
import sptech.classicamoveis.Estabelecimento.Estabelecimento;
import sptech.classicamoveis.Estabelecimento.repository.EstabelecimentoRepository;
import sptech.classicamoveis.Fornecedor.model.Fornecedor;
import sptech.classicamoveis.Fornecedor.repository.FornecedorRepository;
import sptech.classicamoveis.Movimentacao.ItemMovimentacao.ItemMovimentacao;
import sptech.classicamoveis.Movimentacao.ItemMovimentacao.ItemMovimentacaoRepository;
import sptech.classicamoveis.Movimentacao.ItemMovimentacao.dto.ItemMovimentacaoRequestDto;
import sptech.classicamoveis.Movimentacao.Movimentacao;
import sptech.classicamoveis.Movimentacao.MovimentacaoRepository;
import sptech.classicamoveis.Movimentacao.Pagamento.FormaPagamento.FormaPagamento;
import sptech.classicamoveis.Movimentacao.Pagamento.dto.PagamentoRequestDto;
import sptech.classicamoveis.Movimentacao.StatusMovimentacao.StatusMovimentacao;
import sptech.classicamoveis.Movimentacao.TipoMovimentacao.TipoMovimentacao;
import sptech.classicamoveis.Movimentacao.dto.MovimentacaoRequestDto;
import sptech.classicamoveis.Movimentacao.dto.MovimentacaoResponseDto;
import sptech.classicamoveis.Movimentacao.mapper.MovimentacaoMapper;
import sptech.classicamoveis.Produto.model.Produto;
import sptech.classicamoveis.Produto.repository.ProdutoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Cobre os caminhos de sucesso de {@link MovimentacaoService#criar} e as validações
 * de estoque, que o MovimentacaoServiceTest original não alcança.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MovimentacaoServiceCriarTest {

    @Mock MovimentacaoRepository movRepo;
    @Mock ItemMovimentacaoRepository itemRepo;
    @Mock MovimentacaoMapper mapper;
    @Mock ColaboradorRepository colaboradorRepo;
    @Mock ClienteRepository clienteRepo;
    @Mock EstabelecimentoRepository estabelecimentoRepo;
    @Mock FornecedorRepository fornecedorRepo;
    @Mock ProdutoRepository produtoRepo;
    @Mock EstoqueService estoque;
    @InjectMocks MovimentacaoService service;

    private final MovimentacaoResponseDto respostaMapeada = new MovimentacaoResponseDto();

    @BeforeEach
    void configurarMocksComuns() {
        Produto produto = new Produto();
        produto.setId(1);
        produto.setNome("Sofá 3 lugares");

        when(colaboradorRepo.findById(5)).thenReturn(Optional.of(new Colaborador()));
        when(estabelecimentoRepo.findById(1)).thenReturn(Optional.of(new Estabelecimento()));
        when(estabelecimentoRepo.findById(2)).thenReturn(Optional.of(new Estabelecimento()));
        when(clienteRepo.findById(3)).thenReturn(Optional.of(new Cliente()));
        when(fornecedorRepo.findById(4L)).thenReturn(Optional.of(new Fornecedor()));
        when(produtoRepo.findById(1)).thenReturn(Optional.of(produto));
        when(estoque.calcularSaldoDisponivel(anyInt(), anyInt())).thenReturn(100.0);
        when(movRepo.save(any(Movimentacao.class))).thenAnswer(inv -> inv.getArgument(0));
        when(itemRepo.findByMovimentacaoId(any())).thenReturn(List.of());
        when(mapper.toResponseDTO(any(Movimentacao.class))).thenReturn(respostaMapeada);
        when(mapper.toItemResponseDTOList(any())).thenReturn(List.of());
    }

    // ---------- builders ----------

    /** 2 x 100 - 10 de desconto = 190. */
    private ItemMovimentacaoRequestDto item() {
        return new ItemMovimentacaoRequestDto(1, 2, 100.0, 10.0);
    }

    private MovimentacaoRequestDto venda() {
        // total = 190 + frete 1 = 191
        return new MovimentacaoRequestDto(TipoMovimentacao.VENDA,
                List.of(new PagamentoRequestDto(FormaPagamento.PIX, 191.0, 1)),
                "obs", 1.0, 1, null, 3, null, 5, List.of(item()));
    }

    private MovimentacaoRequestDto compra() {
        return new MovimentacaoRequestDto(TipoMovimentacao.COMPRA, null,
                "obs", 0.0, null, 2, null, 4, 5, List.of(item()));
    }

    private MovimentacaoRequestDto transferencia() {
        return new MovimentacaoRequestDto(TipoMovimentacao.TRANSFERENCIA, null,
                "obs", 0.0, 1, 2, null, null, 5, List.of(item()));
    }

    private MovimentacaoRequestDto ajuste(TipoMovimentacao tipo) {
        return new MovimentacaoRequestDto(tipo, null,
                "contagem", 0.0, 1, null, null, null, 5, List.of(item()));
    }

    private Movimentacao salva() {
        ArgumentCaptor<Movimentacao> captor = ArgumentCaptor.forClass(Movimentacao.class);
        verify(movRepo).save(captor.capture());
        return captor.getValue();
    }

    // ---------- sucesso ----------

    @Test
    void criaVendaPendenteComPagamentoEItens() {
        MovimentacaoResponseDto resp = service.criar(venda());

        assertSame(respostaMapeada, resp);
        Movimentacao m = salva();
        assertEquals(StatusMovimentacao.PENDENTE, m.getStatus());
        assertEquals(TipoMovimentacao.VENDA, m.getTipoMovimentacao());
        assertEquals(191.0, m.getValorTotal());
        assertEquals(1.0, m.getFrete());
        assertEquals(1, m.getPagamentos().size());
        assertSame(m, m.getPagamentos().get(0).getMovimentacao());
        assertEquals(FormaPagamento.PIX, m.getPagamentos().get(0).getFormaPagamento());
        assertNotNull(m.getCliente());
        assertNotNull(m.getEstabelecimentoOrigem());
        assertNull(m.getEstabelecimentoDestino());
        assertNull(m.getFornecedor());

        ArgumentCaptor<ItemMovimentacao> itemCaptor = ArgumentCaptor.forClass(ItemMovimentacao.class);
        verify(itemRepo).save(itemCaptor.capture());
        assertEquals(2, itemCaptor.getValue().getQtd());
        assertEquals(100.0, itemCaptor.getValue().getPrecoUnitario());
        assertEquals(10.0, itemCaptor.getValue().getDesconto());
    }

    @Test
    void vendaSemParcelasInformadasAssumeUmaParcela() {
        MovimentacaoRequestDto dto = venda();
        dto.getPagamentos().get(0).setQuantidadeParcelas(null);

        service.criar(dto);

        assertEquals(1, salva().getPagamentos().get(0).getQuantidadeParcelas());
    }

    @Test
    void vendaNoCartaoDeCreditoPodeSerParcelada() {
        MovimentacaoRequestDto dto = venda();
        dto.setPagamentos(List.of(new PagamentoRequestDto(FormaPagamento.CARTAO_CREDITO, 191.0, 6)));

        service.criar(dto);

        assertEquals(6, salva().getPagamentos().get(0).getQuantidadeParcelas());
    }

    @Test
    void vendaComVariosPagamentosSomandoOTotal() {
        MovimentacaoRequestDto dto = venda();
        dto.setPagamentos(List.of(
                new PagamentoRequestDto(FormaPagamento.DINHEIRO, 91.0, 1),
                new PagamentoRequestDto(FormaPagamento.PIX, 100.0, 1)));

        service.criar(dto);

        assertEquals(2, salva().getPagamentos().size());
    }

    @Test
    void itensRepetidosNoMesmoProdutoSaoSomadosNaValidacaoDeEstoque() {
        MovimentacaoRequestDto dto = venda();
        dto.setItens(List.of(item(), item()));
        // 2 x 190 + 1 de frete
        dto.setPagamentos(List.of(new PagamentoRequestDto(FormaPagamento.PIX, 381.0, 1)));
        when(estoque.calcularSaldoDisponivel(1, 1)).thenReturn(4.0);

        service.criar(dto);

        verify(itemRepo, times(2)).save(any(ItemMovimentacao.class));
    }

    @Test
    void criaCompraJaConcluida() {
        service.criar(compra());

        Movimentacao m = salva();
        assertEquals(StatusMovimentacao.CONCLUIDO, m.getStatus());
        assertNotNull(m.getEstabelecimentoDestino());
        assertNotNull(m.getFornecedor());
        assertNull(m.getEstabelecimentoOrigem());
        assertTrue(m.getPagamentos().isEmpty());
        assertEquals(190.0, m.getValorTotal());
    }

    @Test
    void criaTransferenciaConcluidaQuandoHaEstoque() {
        service.criar(transferencia());

        Movimentacao m = salva();
        assertEquals(StatusMovimentacao.CONCLUIDO, m.getStatus());
        assertNotNull(m.getEstabelecimentoOrigem());
        assertNotNull(m.getEstabelecimentoDestino());
    }

    @Test
    void criaAjusteDeEntradaSemChecarEstoque() {
        service.criar(ajuste(TipoMovimentacao.AJUSTE_ENTRADA));

        assertEquals(StatusMovimentacao.CONCLUIDO, salva().getStatus());
        verify(estoque, never()).calcularSaldoDisponivel(anyInt(), anyInt());
    }

    @Test
    void criaAjusteDeSaidaQuandoHaEstoque() {
        service.criar(ajuste(TipoMovimentacao.AJUSTE_SAIDA));

        assertEquals(StatusMovimentacao.CONCLUIDO, salva().getStatus());
        verify(estoque).calcularSaldoDisponivel(1, 1);
    }

    @Test
    void criaDevolucaoComStatusConcluido() {
        MovimentacaoRequestDto dto = ajuste(TipoMovimentacao.DEVOLUÇÃO);

        service.criar(dto);

        assertEquals(StatusMovimentacao.CONCLUIDO, salva().getStatus());
    }

    @Test
    void criarMovimentacaoDelegaParaCriar() {
        assertSame(respostaMapeada, service.criarMovimentacao(compra()));
        verify(movRepo).save(any(Movimentacao.class));
    }

    // ---------- estoque insuficiente ----------

    @Test
    void vendaComEstoqueInsuficienteInformaONomeDoProduto() {
        when(estoque.calcularSaldoDisponivel(1, 1)).thenReturn(1.0);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.criar(venda()));

        assertTrue(ex.getMessage().contains("Sofá 3 lugares"));
        verify(movRepo, never()).save(any());
    }

    @Test
    void vendaComEstoqueInsuficienteEProdutoDesconhecidoUsaIdNaMensagem() {
        when(estoque.calcularSaldoDisponivel(1, 1)).thenReturn(0.0);
        when(produtoRepo.findById(1)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.criar(venda()));

        assertTrue(ex.getMessage().contains("Produto 1"));
    }

    @Test
    void transferenciaComEstoqueInsuficiente() {
        when(estoque.calcularSaldoDisponivel(1, 1)).thenReturn(1.0);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.criar(transferencia()));

        assertTrue(ex.getMessage().contains("Estoque insuficiente"));
    }

    @Test
    void transferenciaComProdutoInexistente() {
        when(produtoRepo.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.criar(transferencia()));
    }

    @Test
    void ajusteDeSaidaComEstoqueInsuficiente() {
        when(estoque.calcularSaldoDisponivel(1, 1)).thenReturn(1.0);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.criar(ajuste(TipoMovimentacao.AJUSTE_SAIDA)));

        assertTrue(ex.getMessage().contains("ajuste de saída"));
    }

    @Test
    void ajusteDeSaidaComEstoqueInsuficienteEProdutoDesconhecido() {
        when(estoque.calcularSaldoDisponivel(1, 1)).thenReturn(0.0);
        when(produtoRepo.findById(1)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.criar(ajuste(TipoMovimentacao.AJUSTE_SAIDA)));

        assertTrue(ex.getMessage().contains("Produto 1"));
    }

    @Test
    void ajusteDeSaidaSomaItensRepetidos() {
        MovimentacaoRequestDto dto = ajuste(TipoMovimentacao.AJUSTE_SAIDA);
        dto.setItens(List.of(item(), item())); // 4 unidades no total
        when(estoque.calcularSaldoDisponivel(1, 1)).thenReturn(3.0);

        assertThrows(IllegalArgumentException.class, () -> service.criar(dto));
    }

    // ---------- entidades não encontradas ----------

    @Test
    void colaboradorNaoEncontrado() {
        when(colaboradorRepo.findById(5)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.criar(venda()));
    }

    @Test
    void estabelecimentoOrigemNaoEncontrado() {
        when(estabelecimentoRepo.findById(1)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.criar(venda()));
    }

    @Test
    void estabelecimentoDestinoNaoEncontrado() {
        when(estabelecimentoRepo.findById(2)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.criar(compra()));
    }

    @Test
    void clienteNaoEncontrado() {
        when(clienteRepo.findById(3)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.criar(venda()));
    }

    @Test
    void fornecedorNaoEncontrado() {
        when(fornecedorRepo.findById(4L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.criar(compra()));
    }

    @Test
    void produtoNaoEncontradoAoCriarItem() {
        when(produtoRepo.findById(1)).thenReturn(Optional.empty());
        // compra não valida estoque, então a falha ocorre na criação do item
        assertThrows(EntityNotFoundException.class, () -> service.criar(compra()));
    }

    // ---------- concluir / listagens / histórico ----------

    @Test
    void buscarVendasPendentesIgnoraPeriodoEDelegaParaListar() {
        when(movRepo.findByTipoMovimentacaoAndStatus(TipoMovimentacao.VENDA, StatusMovimentacao.PENDENTE))
                .thenReturn(List.of());

        assertTrue(service.buscarVendasPendentes(1, LocalDate.now(), LocalDate.now()).isEmpty());
        verify(movRepo).findByTipoMovimentacaoAndStatus(TipoMovimentacao.VENDA, StatusMovimentacao.PENDENTE);
    }

    @Test
    void historicoMapeiaItensEOrdenaPorDataDecrescente() {
        Movimentacao m = new Movimentacao();
        m.setId(9);
        when(movRepo.buscarHistorico(eq(TipoMovimentacao.VENDA), eq(1), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(m)));
        when(itemRepo.findByMovimentacaoId(9)).thenReturn(List.of(new ItemMovimentacao()));

        Page<MovimentacaoResponseDto> pagina = service.buscarHistorico(TipoMovimentacao.VENDA, 1, 2, 5);

        assertEquals(1, pagina.getContent().size());
        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(movRepo).buscarHistorico(eq(TipoMovimentacao.VENDA), eq(1), captor.capture());
        assertEquals(2, captor.getValue().getPageNumber());
        assertEquals(5, captor.getValue().getPageSize());
        assertEquals(Sort.Direction.DESC, captor.getValue().getSort().getOrderFor("dataHora").getDirection());
        verify(mapper).toItemResponseDTOList(any());
    }
}
