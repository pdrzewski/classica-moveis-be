package sptech.classicamoveis.Relatorio;

import org.springframework.stereotype.Service;
import sptech.classicamoveis.Movimentacao.ItemMovimentacao.ItemMovimentacaoRepository;
import sptech.classicamoveis.Movimentacao.TipoMovimentacao.TipoMovimentacao;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RelatorioService {

    private final ItemMovimentacaoRepository itemMovimentacaoRepository;

    public RelatorioService(ItemMovimentacaoRepository itemMovimentacaoRepository) {
        this.itemMovimentacaoRepository = itemMovimentacaoRepository;
    }

    public List<RelatorioVendaItemDto> relatorioVendasPorFornecedor(Integer fornecedorId, Integer idLoja) {
        return itemMovimentacaoRepository.relatorioVendasPorFornecedor(fornecedorId, idLoja);
    }

    public List<RelatorioVendasPorProdutoDto> relatorioVendasPorProduto(
            Integer categoriaId,
            Integer idLoja,
            List<Integer> produtoIds,
            LocalDateTime dataInicio,
            LocalDateTime dataFim) {

        List<RelatorioVendasPorProdutoDto> resultado = itemMovimentacaoRepository.relatorioVendasPorProduto(
                categoriaId, idLoja, dataInicio, dataFim, TipoMovimentacao.VENDA
        );

        if (produtoIds != null && !produtoIds.isEmpty()) {
            return resultado.stream()
                    .filter(dto -> produtoIds.contains(dto.produtoId()))
                    .toList();
        }

        return resultado;
    }
}