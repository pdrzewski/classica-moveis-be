package sptech.classicamoveis.Produto.dto;

public record ProdutoResponseDTO(
        Integer id,
        Long fornecedorId,
        Integer categoriaId,
        String nome,
        String sku,
        String codigoBarras,
        String unidadeMedida,
        String marca,
        Double precoCusto,
        Double precoVenda,
        Integer estoqueMinimo,
        Boolean ativo
) {
}
