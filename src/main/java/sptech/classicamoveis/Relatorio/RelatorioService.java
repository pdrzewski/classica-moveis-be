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
import sptech.classicamoveis.Relatorio.dto.RelatorioVendaDto;
import sptech.classicamoveis.Relatorio.dto.RelatorioVendaPagamentoDto;
import sptech.classicamoveis.Relatorio.dto.RelatorioVendaProdutoDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RelatorioService {

    private final MovimentacaoRepository movimentacaoRepository;
    private final ItemMovimentacaoRepository itemRepository;
    private final PagamentoRepository pagamentoRepository;

    public RelatorioService(
            MovimentacaoRepository movimentacaoRepository,
            ItemMovimentacaoRepository itemRepository,
            PagamentoRepository pagamentoRepository
    ) {
        this.movimentacaoRepository = movimentacaoRepository;
        this.itemRepository = itemRepository;
        this.pagamentoRepository = pagamentoRepository;
    }

    @Transactional(readOnly = true)
    public RelatorioVendaDto gerarRelatorioVenda(Integer movimentacaoId) {

        Movimentacao movimentacao =
                movimentacaoRepository.findById(movimentacaoId)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Venda não encontrada com id: "
                                                + movimentacaoId
                                )
                        );

        RelatorioVendaDto dto =
                new RelatorioVendaDto();

        // =====================================================
        // DADOS PRINCIPAIS
        // =====================================================

        dto.setNumeroPedido(
                movimentacao.getId()
        );

        dto.setDataHoraGeracao(
                LocalDateTime.now()
        );

        dto.setDataHoraEntrega(
                movimentacao.getDataHoraEntrega()
        );

        dto.setFrete(
                movimentacao.getFrete() == null
                        ? 0.0
                        : movimentacao.getFrete()
        );

        // =====================================================
        // CLIENTE
        // =====================================================

        if (movimentacao.getCliente() != null) {

            var cliente =
                    movimentacao.getCliente();

            dto.setNomeCliente(
                    cliente.getNome()
            );

            dto.setDocumentoCliente(
                    cliente.getDocumento()
            );

            dto.setTelefoneCliente(
                    cliente.getTelefone1()
            );

            String endereco =
                    montarEndereco(
                            cliente.getEndereco()
                    );

            dto.setEnderecoCompletoCliente(
                    endereco
            );

            // O endereço de entrega é o mesmo
            dto.setEnderecoEntrega(
                    endereco
            );
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

        List<ItemMovimentacao> itens =
                itemRepository.findByMovimentacaoId(
                        movimentacaoId
                );

        List<RelatorioVendaProdutoDto> produtos =
                new ArrayList<>();

        double totalProdutos = 0.0;
        double totalDesconto = 0.0;

        for (ItemMovimentacao item : itens) {

            double preco =
                    item.getPrecoUnitario() == null
                            ? 0.0
                            : item.getPrecoUnitario();

            int quantidade =
                    item.getQtd() == null
                            ? 0
                            : item.getQtd();

            double desconto =
                    item.getDesconto() == null
                            ? 0.0
                            : item.getDesconto();

            double total =
                    (preco * quantidade)
                            - desconto;

            totalProdutos += total;
            totalDesconto += desconto;

            produtos.add(
                    new RelatorioVendaProdutoDto(
                            item.getProduto().getId(),
                            quantidade,
                            item.getProduto().getNome(),
                            preco,
                            desconto,
                            total
                    )
            );
        }

        dto.setProdutos(produtos);

        dto.setTotalProdutos(
                totalProdutos
        );

        dto.setDesconto(
                totalDesconto
        );

        // =====================================================
        // TOTAL DO PEDIDO
        // =====================================================

        double frete =
                dto.getFrete() == null
                        ? 0.0
                        : dto.getFrete();

        dto.setTotalPedido(
                totalProdutos + frete
        );

        // =====================================================
        // PAGAMENTOS
        // =====================================================

        List<Pagamento> pagamentos =
                pagamentoRepository
                        .findByMovimentacaoId(
                                movimentacaoId
                        );

        List<RelatorioVendaPagamentoDto> pagamentosDto =
                new ArrayList<>();

        for (Pagamento pagamento : pagamentos) {

            String forma =
                    pagamento
                            .getFormaPagamento()
                            .name();

            pagamentosDto.add(
                    new RelatorioVendaPagamentoDto(
                            forma,
                            pagamento.getValor(),
                            pagamento.getQuantidadeParcelas()
                    )
            );
        }

        dto.setPagamentos(
                pagamentosDto
        );

        return dto;
    }

    // =========================================================
    // ENDEREÇO
    // =========================================================

    private String montarEndereco(
            sptech.classicamoveis.Endereco.Endereco endereco
    ) {

        if (endereco == null) {
            return "-";
        }

        StringBuilder texto =
                new StringBuilder();

        if (endereco.getLogradouro() != null) {
            texto.append(
                    endereco.getLogradouro()
            );
        }

        if (endereco.getNumero() != null) {

            if (texto.length() > 0) {
                texto.append(", ");
            }

            texto.append(
                    endereco.getNumero()
            );
        }

        if (endereco.getBairro() != null) {

            if (texto.length() > 0) {
                texto.append(" - ");
            }

            texto.append(
                    endereco.getBairro()
            );
        }

        if (endereco.getCidade() != null) {

            if (texto.length() > 0) {
                texto.append(" - ");
            }

            texto.append(
                    endereco.getCidade()
            );
        }

        if (endereco.getEstado() != null) {

            texto.append(
                    "/"
            );

            texto.append(
                    endereco.getEstado()
            );
        }

        if (endereco.getCep() != null) {

            texto.append(
                    " - CEP "
            );

            texto.append(
                    endereco.getCep()
            );
        }

        return texto.toString();
    }
}