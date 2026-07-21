package sptech.classicamoveis.Movimentacao;

import lombok.Data;
import sptech.classicamoveis.Movimentacao.ItemMovimentacao.ItemMovimentacao;
import java.util.List;

@Data
public class MovimentacaoComItensDto<T> {
    private T dados; // Pode ser Venda, Compra ou Transferencia
    private List<ItemMovimentacao> itens;
}
