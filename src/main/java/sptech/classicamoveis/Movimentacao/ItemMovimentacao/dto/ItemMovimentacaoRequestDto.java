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
@Schema(description = "Item de uma movimentação (venda, compra, transferência etc.)")
public class ItemMovimentacaoRequestDto {
    @Schema(example = "12")
    private Integer produtoId;
    @Schema(example = "2")
    private Integer quantidade;
    @Schema(example = "1599.90")
    private Double valorUnitario;
    @Schema(example = "50.00")
    private Double desconto;

    public Double getSubtotal() {
        return (quantidade * valorUnitario) - (desconto != null ? desconto : 0);
    }
}
