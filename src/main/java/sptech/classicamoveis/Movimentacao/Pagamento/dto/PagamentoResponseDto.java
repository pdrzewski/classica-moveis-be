package sptech.classicamoveis.Movimentacao.Pagamento.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sptech.classicamoveis.Movimentacao.Pagamento.FormaPagamento.FormaPagamento;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Pagamento de uma movimentação")
public class PagamentoResponseDto {

    @Schema(example = "1")
    private Integer id;

    @Schema(example = "DINHEIRO")
    private FormaPagamento formaPagamento;

    @Schema(example = "200.00")
    private Double valor;

    @Schema(example = "2")
    private Integer quantidadeParcelas;
}
