package sptech.classicamoveis.Produto.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados de um produto retornados pela API")
public record ProdutoResponseDTO(
        @Schema(example = "12") Integer id,
        @Schema(example = "1") Long fornecedorId,
        @Schema(example = "3") Integer categoriaId,
        @Schema(example = "Sofá Retrátil 3 Lugares Suede") String nome,
        @Schema(example = "SOF-3L-CINZA-001") String sku,
        @Schema(example = "7891234567895") String codigoBarras,
        @Schema(example = "UN") String unidadeMedida,
        @Schema(example = "Rufato Estofados") String marca,
        @Schema(example = "899.90") Double precoCusto,
        @Schema(example = "1599.90") Double precoVenda,
        @Schema(example = "5") Integer estoqueMinimo,
        @Schema(example = "true") Boolean ativo
) {
}
