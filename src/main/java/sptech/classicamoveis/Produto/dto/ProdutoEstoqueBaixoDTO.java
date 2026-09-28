package sptech.classicamoveis.Produto.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Produto com estoque abaixo ou igual ao mínimo considerando somente o estoque da matriz")
public record ProdutoEstoqueBaixoDTO(
        ProdutoResponseDTO produto,

        @Schema(description = "Estoque atual do produto na matriz", example = "3")
        Long estoqueAtual
) {}
