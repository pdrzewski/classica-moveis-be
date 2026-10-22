package sptech.classicamoveis.Movimentacao.dto;

import java.time.LocalDateTime;

public record ProdutoEstoqueAlertaMensagem(
        Integer produtoId,
        String nome,
        String sku,
        Long estoqueAtual,
        Integer estoqueMinimo,
        String tipoMovimentacao,
        Integer quantidadeMovimentada,
        Integer estabelecimentoId,
        String observacao,
        LocalDateTime dataHora
) {
}

