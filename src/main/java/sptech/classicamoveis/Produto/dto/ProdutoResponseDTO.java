package sptech.classicamoveis.Produto.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados de um produto retornados pela API")
public record ProdutoResponseDTO(
        @Schema(example = "12") Integer id,
        @Schema(example = "Fornecedor ABC") String fornecedor,
        @Schema(example = "Sofá") String categoria,
        @Schema(example = "Sofá Retrátil 3 Lugares Suede") String nome,
        @Schema(example = "SOF-3L-CINZA-001") String sku,
        @Schema(example = "7891234567895") String codigoBarras,
        @Schema(example = "94036000") String ncm,
        @Schema(example = "5102") String cfop,
        @Schema(example = "UN") String unidadeMedida,
        @Schema(example = "Rufato Estofados") String marca,
        @Schema(example = "899.90") Double precoCusto,
        @Schema(example = "1599.90") Double precoVenda,
        @Schema(example = "5") Integer estoqueMinimo,
        @Schema(example = "true") Boolean ativo
) {
    // Compatibilidade com o contrato anterior sem NCM e CFOP.
    public ProdutoResponseDTO(Integer id, Long fornecedor, Integer categoria, String nome,
                              String sku, String codigoBarras, String unidadeMedida,
                              String marca, Double precoCusto, Double precoVenda,
                              Integer estoqueMinimo, Boolean ativo) {
        this(id,
                fornecedor == null ? null : String.valueOf(fornecedor),
                categoria == null ? null : String.valueOf(categoria),
                nome, sku, codigoBarras, null, null, unidadeMedida, marca,
                precoCusto, precoVenda, estoqueMinimo, ativo);
    }
}
