package sptech.classicamoveis.Relatorio;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sptech.classicamoveis.Movimentacao.ItemMovimentacao.ItemMovimentacaoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RelatorioService {


    private final ItemMovimentacaoRepository itemMovimentacaoRepository;

    public List<RelatorioVendaItemDto> relatorioVendasPorFornecedor(Integer fornecedorId, Integer idLoja) {
        return itemMovimentacaoRepository.relatorioVendasPorFornecedor(fornecedorId, idLoja);
    }

    public List<RelatorioVendasPorProdutoDto> relatorioVendasPorProduto(
            Integer categoriaId,
            Integer idLoja,
            List<Integer> produtoIds,
            LocalDateTime dataInicio,
            LocalDateTime dataFim) {

        List<RelatorioVendasPorProdutoDto> resultado =
                itemMovimentacaoRepository.relatorioVendasPorProduto(categoriaId, idLoja, dataInicio, dataFim);

        if (produtoIds != null && !produtoIds.isEmpty()) {
            resultado = resultado.stream()
                    .filter(dto -> produtoIds.contains(dto.produtoId()))
                    .collect(Collectors.toList());
        }

        return resultado;
    }
}