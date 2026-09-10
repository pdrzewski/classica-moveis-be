package sptech.classicamoveis.Produto.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Produto com estoque abaixo (ou igual) ao mínimo, somando todos os estabelecimentos")
public record ProdutoEstoqueBaixoDTO(
        ProdutoResponseDTO produto,

        @Schema(description = "Estoque atual do produto somando todos os estabelecimentos", example = "3")
        Long estoqueAtual
) {
}
