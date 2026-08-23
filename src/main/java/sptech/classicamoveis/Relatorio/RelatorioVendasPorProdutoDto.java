package sptech.classicamoveis.Relatorio;

public record RelatorioVendasPorProdutoDto(
        Integer produtoId,
        String nomeProduto,
        Long quantidadeVendida,
        Double valorTotalVendido
) {}