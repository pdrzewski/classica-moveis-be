package sptech.classicamoveis.Movimentacao.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import sptech.classicamoveis.Movimentacao.Movimentacao;
import sptech.classicamoveis.Movimentacao.ItemMovimentacao.ItemMovimentacao;
import sptech.classicamoveis.Movimentacao.dto.ProdutoEstoqueAlertaMensagem;
import sptech.classicamoveis.Produto.model.Produto;
import sptech.classicamoveis.config.RabbitMqConfig;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoEstoqueAlertaPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final EstoqueService estoqueService;

    public void publishAlertaEstoqueMinimo(Movimentacao movimentacao, List<ItemMovimentacao> itens) {
        if (movimentacao == null || itens == null || itens.isEmpty()) {
            return;
        }

        List<Integer> estabelecimentosAlvo = new ArrayList<>();

        if (movimentacao.getEstabelecimentoOrigem() != null) {
            estabelecimentosAlvo.add(movimentacao.getEstabelecimentoOrigem().getId());
        }

        if (movimentacao.getEstabelecimentoDestino() != null) {
            estabelecimentosAlvo.add(movimentacao.getEstabelecimentoDestino().getId());
        }

        if (estabelecimentosAlvo.isEmpty()) {
            return;
        }

        for (ItemMovimentacao item : itens) {
            Produto produto = item.getProduto();
            if (produto == null || produto.getEstoqueMinimo() == null) {
                continue;
            }

            for (Integer estabelecimentoId : estabelecimentosAlvo) {
                Long saldoAtual = estoqueService.calcularSaldoProduto(estabelecimentoId, produto.getId());

                if (saldoAtual <= produto.getEstoqueMinimo()) {
                    ProdutoEstoqueAlertaMensagem mensagem = new ProdutoEstoqueAlertaMensagem(
                            produto.getId(),
                            produto.getNome(),
                            produto.getSku(),
                            saldoAtual,
                            produto.getEstoqueMinimo(),
                            movimentacao.getTipoMovimentacao() != null ? movimentacao.getTipoMovimentacao().name() : null,
                            item.getQtd(),
                            estabelecimentoId,
                            movimentacao.getObservacao(),
                            LocalDateTime.now()
                    );

                    rabbitTemplate.convertAndSend(
                            RabbitMqConfig.EXCHANGE_NAME,
                            RabbitMqConfig.ROUTING_KEY,
                            mensagem
                    );
                }
            }
        }
    }
}
