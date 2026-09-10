package sptech.classicamoveis.Movimentacao.ItemMovimentacao.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Item de movimentação retornado pela API")
public class ItemMovimentacaoResponseDto {
    @Schema(example = "31")
    private Integer id;
    @Schema(example = "12")
    private Integer produtoId;
    @Schema(example = "Sofá Retrátil 3 Lugares Suede")
    private String produtoNome;
    @Schema(example = "2")
    private Integer quantidade;
    @Schema(example = "1599.90")
    private Double valorUnitario;
    @Schema(example = "50.00")
    private Double desconto;
    @Schema(example = "3149.80")
    private Double subtotal;
}
