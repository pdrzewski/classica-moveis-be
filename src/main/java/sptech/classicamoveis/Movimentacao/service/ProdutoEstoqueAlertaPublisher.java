package sptech.classicamoveis.Movimentacao.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
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

    private static final Logger log = LoggerFactory.getLogger(ProdutoEstoqueAlertaPublisher.class);

    private final RabbitTemplate rabbitTemplate;
    private final EstoqueService estoqueService;

    /**
     * Monta as mensagens de alerta de estoque mínimo e as publica na fila
     * {@code telegram.queue} (via exchange {@code classica.exchange}).
     *
     * As mensagens são montadas imediatamente (enquanto as entidades ainda estão
     * gerenciadas), mas o envio só acontece após o commit da transação, para que
     * o saldo consultado já reflita a movimentação persistida. Quando não há
     * transação ativa, o envio é feito de imediato.
     */
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

        List<ProdutoEstoqueAlertaMensagem> mensagens = new ArrayList<>();

        for (ItemMovimentacao item : itens) {
            Produto produto = item.getProduto();
            if (produto == null || produto.getEstoqueMinimo() == null) {
                continue;
            }

            String tipoMovimentacao = movimentacao.getTipoMovimentacao() != null
                    ? movimentacao.getTipoMovimentacao().name()
                    : null;

            for (Integer estabelecimentoId : estabelecimentosAlvo) {
                Long saldoAtual = estoqueService.calcularSaldoProduto(estabelecimentoId, produto.getId());

                if (saldoAtual <= produto.getEstoqueMinimo()) {
                    mensagens.add(new ProdutoEstoqueAlertaMensagem(
                            produto.getId(),
                            produto.getNome(),
                            produto.getSku(),
                            saldoAtual,
                            produto.getEstoqueMinimo(),
                            tipoMovimentacao,
                            item.getQtd(),
                            estabelecimentoId,
                            movimentacao.getObservacao(),
                            LocalDateTime.now()
                    ));
                }
            }
        }

        if (mensagens.isEmpty()) {
            return;
        }

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    enviar(mensagens);
                }
            });
        } else {
            enviar(mensagens);
        }
    }

    private void enviar(List<ProdutoEstoqueAlertaMensagem> mensagens) {
        for (ProdutoEstoqueAlertaMensagem mensagem : mensagens) {
            try {
                rabbitTemplate.convertAndSend(
                        RabbitMqConfig.EXCHANGE_NAME,
                        RabbitMqConfig.ROUTING_KEY,
                        mensagem
                );

                log.info(
                        "Alerta de estoque mínimo enviado para a fila '{}': produtoId={}, estabelecimentoId={}, saldo={}, minimo={}",
                        RabbitMqConfig.QUEUE_NAME,
                        mensagem.produtoId(),
                        mensagem.estabelecimentoId(),
                        mensagem.estoqueAtual(),
                        mensagem.estoqueMinimo()
                );
            } catch (AmqpException ex) {
                log.error(
                        "Falha ao publicar alerta de estoque mínimo na fila '{}': produtoId={}, estabelecimentoId={}",
                        RabbitMqConfig.QUEUE_NAME,
                        mensagem.produtoId(),
                        mensagem.estabelecimentoId(),
                        ex
                );
            }
        }
    }
}
