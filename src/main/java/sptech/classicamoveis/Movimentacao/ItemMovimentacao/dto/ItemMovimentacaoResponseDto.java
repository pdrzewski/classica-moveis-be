package sptech.classicamoveis.Movimentacao.ItemMovimentacao.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemMovimentacaoResponseDto {
    private Integer id;
    private Integer produtoId;
    private String produtoNome;
    private Integer quantidade;
    private Double valorUnitario;
    private Double desconto;
    private Double subtotal;
}
