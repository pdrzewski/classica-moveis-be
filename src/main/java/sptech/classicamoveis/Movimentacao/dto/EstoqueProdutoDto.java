package sptech.classicamoveis.Movimentacao.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import sptech.classicamoveis.Produto.dto.ProdutoResponseDTO;

@Schema(description = "Saldo de estoque de um produto em um estabelecimento, já com os dados completos do produto")
public record EstoqueProdutoDto(

        ProdutoResponseDTO produto,

        @Schema(description = "Id do estabelecimento consultado", example = "1")
        Integer estabelecimentoId,

        @Schema(description = "Saldo atual do produto no estabelecimento", example = "12")
        Long saldo
) {
}
