package sptech.classicamoveis.Produto.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para criação/atualização de um produto")
public record ProdutoRequestDTO(

        @NotNull(message = "O id do fornecedor é obrigatório")
        @Schema(description = "Id do fornecedor do produto", example = "1")
        Long fornecedorId,

        @NotNull(message = "O id da categoria é obrigatório")
        @Schema(description = "Id da categoria do produto", example = "3")
        Integer categoriaId,

        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 45, message = "Nome deve ter no máximo 45 caracteres")
        @Schema(description = "Nome do produto", example = "Sofá Retrátil 3 Lugares Suede")
        String nome,

        @Size(max = 45, message = "SKU deve ter no máximo 45 caracteres")
        @Schema(description = "Código SKU interno", example = "SOF-3L-CINZA-001")
        String sku,

        @Size(max = 45, message = "Código de barras deve ter no máximo 45 caracteres")
        @Schema(description = "Código de barras (EAN)", example = "7891234567895")
        String codigoBarras,

        @Size(max = 8, message = "NCM deve ter no máximo 8 caracteres")
        @jakarta.validation.constraints.Pattern(regexp = "\\d{8}", message = "NCM deve conter 8 números")
        @Schema(description = "Nomenclatura Comum do Mercosul", example = "94036000")
        String ncm,

        @Size(max = 4, message = "CFOP deve ter no máximo 4 caracteres")
        @jakarta.validation.constraints.Pattern(regexp = "\\d{4}", message = "CFOP deve conter 4 números")
        @Schema(description = "Código Fiscal de Operações e Prestações", example = "5102")
        String cfop,

        @Size(max = 45, message = "Unidade de medida deve ter no máximo 45 caracteres")
        @Schema(description = "Unidade de medida", example = "UN")
        String unidadeMedida,

        @Size(max = 45, message = "Marca deve ter no máximo 45 caracteres")
        @Schema(description = "Marca/fabricante do produto", example = "Rufato Estofados")
        String marca,

        @NotNull(message = "Preço de custo é obrigatório")
        @PositiveOrZero(message = "Preço de custo não pode ser negativo")
        @Schema(description = "Preço de custo (compra)", example = "899.90")
        Double precoCusto,

        @NotNull(message = "Preço de venda é obrigatório")
        @PositiveOrZero(message = "Preço de venda não pode ser negativo")
        @Schema(description = "Preço de venda ao cliente final", example = "1599.90")
        Double precoVenda,

        @NotNull(message = "Estoque mínimo é obrigatório")
        @PositiveOrZero(message = "Estoque mínimo não pode ser negativo")
        @Schema(description = "Quantidade mínima em estoque antes de alertar reposição", example = "5")
        Integer estoqueMinimo,

        @NotNull(message = "Status ativo é obrigatório")
        @Schema(description = "Indica se o produto está ativo para venda", example = "true")
        Boolean ativo
) {
        // Compatibilidade com o contrato anterior sem NCM e CFOP.
        public ProdutoRequestDTO(Long fornecedorId, Integer categoriaId, String nome,
                                 String sku, String codigoBarras, String unidadeMedida,
                                 String marca, Double precoCusto, Double precoVenda,
                                 Integer estoqueMinimo, Boolean ativo) {
                this(fornecedorId, categoriaId, nome, sku, codigoBarras, null, null,
                        unidadeMedida, marca, precoCusto, precoVenda, estoqueMinimo, ativo);
        }
}
