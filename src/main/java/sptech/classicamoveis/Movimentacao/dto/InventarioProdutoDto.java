package sptech.classicamoveis.Movimentacao.dto;

public record InventarioProdutoDto(
        Integer produtoId,
        String produto,
        String marca,
        String fornecedora,
        String categoria,
        Long quantidadeAtual,
        Integer quantidadeContada
) {
}