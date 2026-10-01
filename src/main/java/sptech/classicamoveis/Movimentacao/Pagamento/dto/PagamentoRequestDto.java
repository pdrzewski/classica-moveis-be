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
@Schema(description = "Forma de pagamento utilizada em uma movimentação")
public class PagamentoRequestDto {

    @Schema(example = "DINHEIRO")
    private FormaPagamento formaPagamento;

    @Schema(example = "200.00")
    private Double valor;

    @Schema(example = "1", defaultValue = "1")
    private Integer quantidadeParcelas;
}