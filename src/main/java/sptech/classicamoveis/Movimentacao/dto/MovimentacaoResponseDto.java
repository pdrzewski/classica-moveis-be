package sptech.classicamoveis.Movimentacao.dto;

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
public class MovimentacaoResponseDto {
    private Integer id;
    private LocalDateTime dataHora;
    private TipoMovimentacao tipoMovimentacao;
    private StatusMovimentacao status;
    private FormaPagamento formaPagamento;
    private String observacao;
    private Double valorTotal;
    
    // Identificadores
    private Integer colaboradorId;
    private String colaboradorNome;
    private Integer estabelecimentoOrigemId;
    private String estabelecimentoOrigemNome;
    private Integer estabelecimentoDestinoId;
    private String estabelecimentoDestinoNome;
    private Integer clienteId;
    private String clienteNome;
    private Integer fornecedorId;
    private String fornecedorNome;
    
    // Itens da movimentação
    private List<ItemMovimentacaoResponseDto> itens;
}
