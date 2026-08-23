package sptech.classicamoveis.Produto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record ProdutoRequestDTO(
        @NotNull(message = "O id do fornecedor é obrigatório")
        Long fornecedorId,

        @NotNull(message = "O id da categoria é obrigatório")
        Integer categoriaId,

        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 45, message = "Nome deve ter no máximo 45 caracteres")
        String nome,

        @Size(max = 45, message = "SKU deve ter no máximo 45 caracteres")
        String sku,

        @Size(max = 45, message = "Código de barras deve ter no máximo 45 caracteres")
        String codigoBarras,

        @Size(max = 45, message = "Unidade de medida deve ter no máximo 45 caracteres")
        String unidadeMedida,

        @Size(max = 45, message = "Marca deve ter no máximo 45 caracteres")
        String marca,

        @NotNull(message = "Preço de custo é obrigatório")
        @PositiveOrZero(message = "Preço de custo não pode ser negativo")
        Double precoCusto,

        @NotNull(message = "Preço de venda é obrigatório")
        @PositiveOrZero(message = "Preço de venda não pode ser negativo")
        Double precoVenda,

        @NotNull(message = "Estoque mínimo é obrigatório")
        @PositiveOrZero(message = "Estoque mínimo não pode ser negativo")
        Integer estoqueMinimo,

        @NotNull(message = "Status ativo é obrigatório")
        Boolean ativo
) {
}
