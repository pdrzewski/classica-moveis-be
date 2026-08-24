package sptech.classicamoveis.Movimentacao.dto;

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
public class MovimentacaoRequestDto {
    private TipoMovimentacao tipoMovimentacao;
    private FormaPagamento formaPagamento;
    private String observacao;
    
    // Identificadores de origem/destino
    private Integer estabelecimentoOrigemId;
    private Integer estabelecimentoDestinoId;
    private Integer clienteId;
    private Integer fornecedorId;
    private Integer colaboradorId;
    
    // Itens da movimentação
    private List<ItemMovimentacaoRequestDto> itens;
}
