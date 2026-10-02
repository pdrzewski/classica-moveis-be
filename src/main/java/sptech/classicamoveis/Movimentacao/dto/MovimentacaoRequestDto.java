package sptech.classicamoveis.Movimentacao.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sptech.classicamoveis.Movimentacao.TipoMovimentacao.TipoMovimentacao;
import sptech.classicamoveis.Movimentacao.Pagamento.dto.PagamentoRequestDto;
import sptech.classicamoveis.Movimentacao.ItemMovimentacao.dto.ItemMovimentacaoRequestDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados para registrar uma movimentação")
public class MovimentacaoRequestDto {

    @Schema(example = "VENDA")
    private TipoMovimentacao tipoMovimentacao;

    private List<PagamentoRequestDto> pagamentos;

    @Schema(example = "Venda balcão - cliente retirou na loja")
    private String observacao;

    @Schema(example = "150.00")
    private Double frete;

    @Schema(description = "Estabelecimento de onde os itens saem", example = "1")
    private Integer estabelecimentoOrigemId;

    @Schema(description = "Estabelecimento de destino", example = "2")
    private Integer estabelecimentoDestinoId;

    @Schema(description = "Cliente da venda", example = "8")
    private Integer clienteId;

    @Schema(description = "Fornecedor da compra", example = "4")
    private Integer fornecedorId;

    @Schema(description = "Colaborador responsável", example = "5")
    private Integer colaboradorId;

    private List<ItemMovimentacaoRequestDto> itens;
}