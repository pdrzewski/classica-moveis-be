package sptech.classicamoveis.Movimentacao.ItemMovimentacao.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemMovimentacaoRequestDto {
    private Integer produtoId;
    private Integer quantidade;
    private Double valorUnitario;
    private Double desconto;

    public Double getSubtotal() {
        return (quantidade * valorUnitario) - (desconto != null ? desconto : 0);
    }
}
