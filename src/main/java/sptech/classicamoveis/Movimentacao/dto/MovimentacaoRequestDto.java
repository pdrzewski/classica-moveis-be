package sptech.classicamoveis.Movimentacao.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sptech.classicamoveis.Movimentacao.TipoMovimentacao.TipoMovimentacao;
import sptech.classicamoveis.Movimentacao.FormaPagamento.FormaPagamento;
import sptech.classicamoveis.Movimentacao.ItemMovimentacao.dto.ItemMovimentacaoRequestDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados para registrar uma movimentação (venda, compra, transferência, ajuste ou devolução)")
public class MovimentacaoRequestDto {
    @Schema(example = "VENDA")
    private TipoMovimentacao tipoMovimentacao;
    @Schema(example = "PIX")
    private FormaPagamento formaPagamento;
    @Schema(example = "Venda balcão - cliente retirou na loja")
    private String observacao;

    // Identificadores de origem/destino
    @Schema(description = "Estabelecimento de onde os itens saem (venda/transferência)", example = "1")
    private Integer estabelecimentoOrigemId;
    @Schema(description = "Estabelecimento de destino (compra/transferência)", example = "2")
    private Integer estabelecimentoDestinoId;
    @Schema(description = "Preenchido em vendas/devoluções", example = "8")
    private Integer clienteId;
    @Schema(description = "Preenchido em compras", example = "4")
    private Integer fornecedorId;
    @Schema(description = "Colaborador responsável pela movimentação", example = "5")
    private Integer colaboradorId;

    // Itens da movimentação
    private List<ItemMovimentacaoRequestDto> itens;
}
