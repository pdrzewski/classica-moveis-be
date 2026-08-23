package sptech.classicamoveis.Estoque;

public record RelatorioEstoqueItemDto(
        Integer produtoId,
        String nomeProduto,
        Integer quantidadeAtual,
        Integer estoqueMinimo,
        boolean abaixoDoMinimo
) {}
