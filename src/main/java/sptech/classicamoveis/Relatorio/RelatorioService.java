package sptech.classicamoveis.Relatorio;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sptech.classicamoveis.Movimentacao.ItemMovimentacao.ItemMovimentacao;
import sptech.classicamoveis.Movimentacao.ItemMovimentacao.ItemMovimentacaoRepository;
import sptech.classicamoveis.Movimentacao.Movimentacao;
import sptech.classicamoveis.Movimentacao.MovimentacaoRepository;
import sptech.classicamoveis.Movimentacao.Pagamento.Pagamento;
import sptech.classicamoveis.Movimentacao.Pagamento.repository.PagamentoRepository;
import sptech.classicamoveis.Colaborador.model.Colaborador;
import sptech.classicamoveis.Colaborador.repository.ColaboradorRepository;
import sptech.classicamoveis.Movimentacao.StatusMovimentacao.StatusMovimentacao;
import sptech.classicamoveis.Movimentacao.TipoMovimentacao.TipoMovimentacao;
import sptech.classicamoveis.Relatorio.dto.RelatorioVendaPorVendedorDto;
import sptech.classicamoveis.Relatorio.dto.RelatorioVendaVendedorItemDto;
import sptech.classicamoveis.Relatorio.dto.RelatorioVendaVendedorVendaDto;
import sptech.classicamoveis.Relatorio.dto.RelatorioVendaDto;
import sptech.classicamoveis.Relatorio.dto.RelatorioVendaPagamentoDto;
import sptech.classicamoveis.Relatorio.dto.RelatorioVendaProdutoDto;
import sptech.classicamoveis.Estabelecimento.Estabelecimento;
import sptech.classicamoveis.Estabelecimento.repository.EstabelecimentoRepository;
import sptech.classicamoveis.Fornecedor.model.Fornecedor;
import sptech.classicamoveis.Fornecedor.repository.FornecedorRepository;
import sptech.classicamoveis.Movimentacao.service.EstoqueService;
import sptech.classicamoveis.Produto.model.Produto;
import sptech.classicamoveis.Produto.repository.ProdutoRepository;
import sptech.classicamoveis.Relatorio.dto.RelatorioTabelaPrecosDto;
import sptech.classicamoveis.Relatorio.dto.RelatorioTabelaPrecosItemDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RelatorioService {

    private final MovimentacaoRepository movimentacaoRepository;
    private final ItemMovimentacaoRepository itemRepository;
    private final PagamentoRepository pagamentoRepository;
    private final ColaboradorRepository colaboradorRepository;
    private final ProdutoRepository produtoRepository;
    private final EstoqueService estoqueService;
    private final EstabelecimentoRepository estabelecimentoRepository;
    private final FornecedorRepository fornecedorRepository;

    public RelatorioService(
            MovimentacaoRepository movimentacaoRepository,
            ItemMovimentacaoRepository itemRepository,
            PagamentoRepository pagamentoRepository,
            ColaboradorRepository colaboradorRepository,
            ProdutoRepository produtoRepository,
            EstoqueService estoqueService,
            EstabelecimentoRepository estabelecimentoRepository,
            FornecedorRepository fornecedorRepository
    ) {
        this.movimentacaoRepository = movimentacaoRepository;
        this.itemRepository = itemRepository;
        this.pagamentoRepository = pagamentoRepository;
        this.colaboradorRepository = colaboradorRepository;
        this.produtoRepository = produtoRepository;
        this.estoqueService = estoqueService;
        this.estabelecimentoRepository = estabelecimentoRepository;
        this.fornecedorRepository = fornecedorRepository;
    }

    @Transactional(readOnly = true)
    public RelatorioVendaPorVendedorDto gerarRelatorioVendaPorVendedor(
            Integer colaboradorId,
            java.time.LocalDate dataInicio,
            java.time.LocalDate dataFim
    ) {

        Colaborador colaborador = colaboradorRepository.findById(colaboradorId)
                .orElseThrow(() -> new EntityNotFoundException("Colaborador não encontrado com id: " + colaboradorId));

        java.time.LocalDate hoje = java.time.LocalDate.now();

        if (dataInicio == null) {
            dataInicio = hoje.withDayOfMonth(1);
        }

        if (dataFim == null) {
            dataFim = hoje;
        }

        if (dataFim.isBefore(dataInicio)) {
            throw new IllegalArgumentException(
                    "A data fim não pode ser anterior à data início"
            );
        }

        java.time.LocalDateTime inicio = dataInicio.atStartOfDay();
        java.time.LocalDateTime fim = dataFim.plusDays(1).atStartOfDay();

        List<Movimentacao> vendas = movimentacaoRepository.buscarVendasPorVendedor(
                colaboradorId,
                inicio,
                fim,
                TipoMovimentacao.VENDA,
                StatusMovimentacao.CANCELADO
        );

        RelatorioVendaPorVendedorDto dto = new RelatorioVendaPorVendedorDto();
        dto.setDataInicio(dataInicio);
        dto.setDataFim(dataFim);
        dto.setVendedorId(colaborador.getId());
        dto.setVendedorNome(colaborador.getNome());
        dto.setSalario(colaborador.getSalario());
        dto.setComissao(colaborador.getComissao());
        dto.setCargo(colaborador.getCargo() != null ? colaborador.getCargo().getCargo() : null);

        List<RelatorioVendaVendedorVendaDto> vendasDto = new ArrayList<>();
        double totalVendido = 0.0;

        for (Movimentacao venda : vendas) {

            List<ItemMovimentacao> itens = itemRepository.findByMovimentacaoId(venda.getId());

            List<RelatorioVendaVendedorItemDto> itensDto = new ArrayList<>();

            for (ItemMovimentacao item : itens) {
                double precoUnitario = item.getPrecoUnitario() == null ? 0.0 : item.getPrecoUnitario();

                int quantidade = item.getQtd() == null ? 0 : item.getQtd();

                double desconto = item.getDesconto() == null ? 0.0 : item.getDesconto();

                double totalItem = (precoUnitario * quantidade) - desconto;

                itensDto.add(new RelatorioVendaVendedorItemDto(
                        item.getProduto().getId(),
                        item.getProduto().getSku(),
                        item.getProduto().getNome(),
                        quantidade,
                        precoUnitario,
                        totalItem
                ));
            }

            double valorTotalVenda = venda.getValorTotal() == null ? 0.0 : venda.getValorTotal();

            totalVendido += valorTotalVenda;

            vendasDto.add(new RelatorioVendaVendedorVendaDto(
                    venda.getId(),
                    venda.getDataHora(),
                    valorTotalVenda,
                    itensDto
            ));
        }

        dto.setVendas(vendasDto);
        dto.setTotalVendido(totalVendido);

        double percentualComissao = colaborador.getComissao() == null ? 0.0 : colaborador.getComissao();

        dto.setValorComissao(totalVendido * percentualComissao / 100.0);

        return dto;
    }

    @Transactional(readOnly = true)
    public RelatorioTabelaPrecosDto gerarTabelaPrecos(Integer estabelecimentoId, Long fornecedorId) {

        Estabelecimento estabelecimento = estabelecimentoRepository.findById(estabelecimentoId)
                .orElseThrow(() -> new EntityNotFoundException("Estabelecimento não encontrado com id: " + estabelecimentoId));

        Fornecedor fornecedor = null;

        if (fornecedorId != null) {
            fornecedor = fornecedorRepository.findById(fornecedorId)
                    .orElseThrow(() -> new EntityNotFoundException("Fornecedor não encontrado com id: " + fornecedorId));
        }

        List<Produto> produtos;

        if (fornecedorId != null) {

            produtos =
                    produtoRepository.findByFornecedorId(
                            fornecedorId
                    ).stream().filter(produto ->
                            Boolean.TRUE.equals(
                                    produto.getAtivo()
                            )
                    ).sorted((p1, p2) -> {

                        String nome1 = p1.getNome() == null ? "" : p1.getNome();
                        String nome2 = p2.getNome() == null ? "" : p2.getNome();

                        return nome1.compareToIgnoreCase(nome2);
                    }).toList();

        } else {

            produtos =
                    produtoRepository.findByAtivoTrue().stream().sorted((p1, p2) -> {

                        String nome1 = p1.getNome() == null ? "" : p1.getNome();
                        String nome2 = p2.getNome() == null ? "" : p2.getNome();

                        return nome1.compareToIgnoreCase(nome2);
                    }).toList();
        }

        RelatorioTabelaPrecosDto dto = new RelatorioTabelaPrecosDto();

        dto.setEstabelecimento(estabelecimento.getNome());
        dto.setFornecedor(fornecedor == null ? "Todos os fornecedores" : fornecedor.getNome());

        List<RelatorioTabelaPrecosItemDto> itens = new ArrayList<>();

        for (Produto produto : produtos) {

            Long quantidadeEstoque =
                    estoqueService.calcularSaldoProduto(
                            estabelecimentoId,
                            produto.getId()
                    );

            itens.add(
                    new RelatorioTabelaPrecosItemDto(
                            produto.getSku(),
                            produto.getNome(),
                            quantidadeEstoque,
                            produto.getPrecoCusto(),
                            produto.getPrecoVenda()
                    )
            );
        }

        dto.setProdutos(itens);

        return dto;
    }

    @Transactional(readOnly = true)
    public RelatorioVendaDto gerarRelatorioVenda(Integer movimentacaoId) {

        Movimentacao movimentacao = movimentacaoRepository.findById(movimentacaoId)
                .orElseThrow(() -> new EntityNotFoundException("Venda não encontrada com id: " + movimentacaoId));

        RelatorioVendaDto dto = new RelatorioVendaDto();

        // =====================================================
        // DADOS PRINCIPAIS
        // =====================================================

        dto.setNumeroPedido(movimentacao.getId());
        dto.setDataHoraGeracao(LocalDateTime.now());
        dto.setDataHoraEntrega(movimentacao.getDataHoraEntrega());
        dto.setFrete(movimentacao.getFrete() == null ? 0.0 : movimentacao.getFrete());

        // =====================================================
        // CLIENTE
        // =====================================================

        if (movimentacao.getCliente() != null) {

            var cliente = movimentacao.getCliente();
            dto.setNomeCliente(cliente.getNome());
            dto.setDocumentoCliente(cliente.getDocumento());
            dto.setTelefoneCliente(cliente.getTelefone1());
            String endereco = montarEndereco(cliente.getEndereco());
            dto.setEnderecoCompletoCliente(endereco);
            dto.setEnderecoEntrega(endereco);
        }

        // =====================================================
        // VENDEDOR
        // =====================================================

        if (movimentacao.getColaborador() != null) {

            dto.setNomeVendedor(
                    movimentacao
                            .getColaborador()
                            .getNome()
            );
        }

        // =====================================================
        // PRODUTOS
        // =====================================================

        List<ItemMovimentacao> itens = itemRepository.findByMovimentacaoId(movimentacaoId);

        List<RelatorioVendaProdutoDto> produtos = new ArrayList<>();

        double totalProdutos = 0.0;
        double totalDesconto = 0.0;

        for (ItemMovimentacao item : itens) {

            double preco = item.getPrecoUnitario() == null ? 0.0 : item.getPrecoUnitario();

            int quantidade = item.getQtd() == null ? 0 : item.getQtd();

            double desconto = item.getDesconto() == null ? 0.0 : item.getDesconto();

            double total = (preco * quantidade) - desconto;

            totalProdutos += total;
            totalDesconto += desconto;

            produtos.add(
                    new RelatorioVendaProdutoDto(
                            item.getProduto().getSku(),
                            quantidade,
                            item.getProduto().getNome(),
                            preco,
                            desconto,
                            total
                    )
            );
        }

        dto.setProdutos(produtos);
        dto.setTotalProdutos(totalProdutos);
        dto.setDesconto(totalDesconto);

        // =====================================================
        // TOTAL DO PEDIDO
        // =====================================================

        double frete = dto.getFrete() == null ? 0.0 : dto.getFrete();

        dto.setTotalPedido(totalProdutos + frete);

        // =====================================================
        // PAGAMENTOS
        // =====================================================

        List<Pagamento> pagamentos = pagamentoRepository.findByMovimentacaoId(movimentacaoId);

        List<RelatorioVendaPagamentoDto> pagamentosDto = new ArrayList<>();

        for (Pagamento pagamento : pagamentos) {

            String forma = pagamento.getFormaPagamento().name();

            pagamentosDto.add(new RelatorioVendaPagamentoDto(forma, pagamento.getValor(), pagamento.getQuantidadeParcelas()));
        }

        dto.setPagamentos(pagamentosDto);

        return dto;
    }

    // =========================================================
    // ENDEREÇO
    // =========================================================

    private String montarEndereco(
            sptech.classicamoveis.Endereco.Endereco endereco
    ) {

        if (endereco == null) {return "-";}

        StringBuilder texto = new StringBuilder();

        if (endereco.getLogradouro() != null) {
            texto.append(endereco.getLogradouro());
        }

        if (endereco.getNumero() != null) {
            if (texto.length() > 0) {texto.append(", ");}
            texto.append(endereco.getNumero());
        }

        if (endereco.getBairro() != null) {
            if (texto.length() > 0) {texto.append(" - ");}
            texto.append(endereco.getBairro());
        }

        if (endereco.getCidade() != null) {
            if (texto.length() > 0) {texto.append(" - ");}
            texto.append(endereco.getCidade());
        }

        if (endereco.getEstado() != null) {
            texto.append("/");
            texto.append(endereco.getEstado());
        }

        if (endereco.getCep() != null) {
            texto.append(" - CEP ");
            texto.append(endereco.getCep());
        }

        return texto.toString();
    }
}