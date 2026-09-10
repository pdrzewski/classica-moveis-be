package sptech.classicamoveis.Movimentacao.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sptech.classicamoveis.Movimentacao.TipoMovimentacao.TipoMovimentacao;
import sptech.classicamoveis.Movimentacao.StatusMovimentacao.StatusMovimentacao;
import sptech.classicamoveis.Movimentacao.FormaPagamento.FormaPagamento;
import sptech.classicamoveis.Movimentacao.ItemMovimentacao.dto.ItemMovimentacaoResponseDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados de uma movimentação retornados pela API")
public class MovimentacaoResponseDto {
    @Schema(example = "101")
    private Integer id;
    @Schema(example = "2026-09-09T14:32:00")
    private LocalDateTime dataHora;
    @Schema(example = "VENDA")
    private TipoMovimentacao tipoMovimentacao;
    @Schema(example = "CONCLUIDO")
    private StatusMovimentacao status;
    @Schema(example = "PIX")
    private FormaPagamento formaPagamento;
    @Schema(example = "Venda balcão - cliente retirou na loja")
    private String observacao;
    @Schema(example = "3149.80")
    private Double valorTotal;

    // Identificadores
    @Schema(example = "5")
    private Integer colaboradorId;
    @Schema(example = "João Pedro Lima")
    private String colaboradorNome;
    @Schema(example = "1")
    private Integer estabelecimentoOrigemId;
    @Schema(example = "Classica Móveis - Matriz Tatuapé")
    private String estabelecimentoOrigemNome;
    @Schema(example = "2")
    private Integer estabelecimentoDestinoId;
    @Schema(example = "Classica Móveis - Filial ABC")
    private String estabelecimentoDestinoNome;
    @Schema(example = "8")
    private Integer clienteId;
    @Schema(example = "Maria Aparecida Souza")
    private String clienteNome;
    @Schema(example = "4")
    private Integer fornecedorId;
    @Schema(example = "Rufato Estofados Ltda")
    private String fornecedorNome;

    // Itens da movimentação
    private List<ItemMovimentacaoResponseDto> itens;
}
