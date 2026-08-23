package sptech.classicamoveis.Relatorio;

public record RelatorioEstoqueItemDto(
        Integer produtoId,
        String nomeProduto,
        Integer quantidadeAtual,
        Integer estoqueMinimo,
        boolean abaixoDoMinimo
) {}
