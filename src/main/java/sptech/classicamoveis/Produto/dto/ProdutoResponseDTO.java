package sptech.classicamoveis.Produto.dto;

import sptech.classicamoveis.Categoria.Categoria;
import sptech.classicamoveis.Fornecedor.model.Fornecedor;

public record ProdutoResponseDTO(
        String nome,
        Fornecedor fornecedor,
        Categoria categoria,
        Double precoCusto,
        Integer estoqueMin,
        String ncm
) {
}
