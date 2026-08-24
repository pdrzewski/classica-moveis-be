package sptech.classicamoveis.Movimentacao.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sptech.classicamoveis.Movimentacao.Movimentacao;
import sptech.classicamoveis.Movimentacao.ItemMovimentacao.ItemMovimentacao;
import sptech.classicamoveis.Movimentacao.ItemMovimentacao.ItemMovimentacaoRepository;
import sptech.classicamoveis.Movimentacao.TipoMovimentacao.TipoMovimentacao;
import sptech.classicamoveis.Movimentacao.StatusMovimentacao.StatusMovimentacao;
import sptech.classicamoveis.Movimentacao.MovimentacaoRepository;
import sptech.classicamoveis.Produto.model.Produto;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EstoqueService {

    private final MovimentacaoRepository movimentacaoRepository;
    private final ItemMovimentacaoRepository itemRepository;


    /**
     * Calcula o saldo dinâmico de um produto em um estabelecimento
     * com base no histórico de movimentações com status CONCLUIDO
     */
    public double calcularSaldoDisponivel(Integer produtoId, Integer estabelecimentoId) {
        double entradas = calcularEntradas(produtoId, estabelecimentoId);
        double saidas = calcularSaidas(produtoId, estabelecimentoId);
        return entradas - saidas;
    }

    /**
     * Lista todas as movimentações que compuseram o saldo
     */
    public List<Movimentacao> obterExtrato(Integer produtoId, Integer estabelecimentoId) {
        List<ItemMovimentacao> itens = itemRepository.findByProdutoIdAndEstabelecimentoId(produtoId, estabelecimentoId);
        List<Movimentacao> movimentacoes = new java.util.ArrayList<>();
        java.util.Set<Integer> processados = new java.util.HashSet<>();
        
        for (ItemMovimentacao item : itens) {
            Movimentacao m = item.getMovimentacao();
            if (m.getStatus().equals(StatusMovimentacao.CONCLUIDO) && !processados.contains(m.getId())) {
                movimentacoes.add(m);
                processados.add(m.getId());
            }
        }
        return movimentacoes;
    }

    private double calcularEntradas(Integer produtoId, Integer estabelecimentoId) {
        double compras = 0;
        double transferencias = 0;
        double ajustesEntrada = 0;

        // COMPRA: estabelecimento é destino
        List<Movimentacao> comprasList = movimentacaoRepository.findByTipoMovimentacaoAndStatusAndEstabelecimentoDestinoId(
                TipoMovimentacao.COMPRA,
                StatusMovimentacao.CONCLUIDO,
                estabelecimentoId
        );
        for (Movimentacao m : comprasList) {
            List<ItemMovimentacao> itens = itemRepository.findByMovimentacaoId(m.getId());
            for (ItemMovimentacao item : itens) {
                if (item.getProduto().getId().equals(produtoId)) {
                    compras += item.getQtd();
                }
            }
        }

        // TRANSFERENCIA: estabelecimento é destino
        List<Movimentacao> transferenciasList = movimentacaoRepository.findByTipoMovimentacaoAndStatusAndEstabelecimentoDestinoId(
                TipoMovimentacao.TRANSFERENCIA,
                StatusMovimentacao.CONCLUIDO,
                estabelecimentoId
        );
        for (Movimentacao m : transferenciasList) {
            List<ItemMovimentacao> itens = itemRepository.findByMovimentacaoId(m.getId());
            for (ItemMovimentacao item : itens) {
                if (item.getProduto().getId().equals(produtoId)) {
                    transferencias += item.getQtd();
                }
            }
        }

        // AJUSTE_ENTRADA
        List<Movimentacao> ajustesList = movimentacaoRepository.findByTipoMovimentacaoAndStatusAndEstabelecimentoOrigemId(
                TipoMovimentacao.AJUSTE_ENTRADA,
                StatusMovimentacao.CONCLUIDO,
                estabelecimentoId
        );
        for (Movimentacao m : ajustesList) {
            List<ItemMovimentacao> itens = itemRepository.findByMovimentacaoId(m.getId());
            for (ItemMovimentacao item : itens) {
                if (item.getProduto().getId().equals(produtoId)) {
                    ajustesEntrada += item.getQtd();
                }
            }
        }

        return compras + transferencias + ajustesEntrada;
    }

    private double calcularSaidas(Integer produtoId, Integer estabelecimentoId) {
        double vendas = 0;
        double transferencias = 0;
        double ajustesSaida = 0;

        // VENDA com status CONCLUIDO: estabelecimento é origem
        List<Movimentacao> vendasList = movimentacaoRepository.findByTipoMovimentacaoAndStatusAndEstabelecimentoOrigemId(
                TipoMovimentacao.VENDA,
                StatusMovimentacao.CONCLUIDO,
                estabelecimentoId
        );
        for (Movimentacao m : vendasList) {
            List<ItemMovimentacao> itens = itemRepository.findByMovimentacaoId(m.getId());
            for (ItemMovimentacao item : itens) {
                if (item.getProduto().getId().equals(produtoId)) {
                    vendas += item.getQtd();
                }
            }
        }

        // TRANSFERENCIA: estabelecimento é origem
        List<Movimentacao> transferenciasList = movimentacaoRepository.findByTipoMovimentacaoAndStatusAndEstabelecimentoOrigemId(
                TipoMovimentacao.TRANSFERENCIA,
                StatusMovimentacao.CONCLUIDO,
                estabelecimentoId
        );
        for (Movimentacao m : transferenciasList) {
            List<ItemMovimentacao> itens = itemRepository.findByMovimentacaoId(m.getId());
            for (ItemMovimentacao item : itens) {
                if (item.getProduto().getId().equals(produtoId)) {
                    transferencias += item.getQtd();
                }
            }
        }

        // AJUSTE_SAIDA
        List<Movimentacao> ajustesList = movimentacaoRepository.findByTipoMovimentacaoAndStatusAndEstabelecimentoOrigemId(
                TipoMovimentacao.AJUSTE_SAIDA,
                StatusMovimentacao.CONCLUIDO,
                estabelecimentoId
        );
        for (Movimentacao m : ajustesList) {
            List<ItemMovimentacao> itens = itemRepository.findByMovimentacaoId(m.getId());
            for (ItemMovimentacao item : itens) {
                if (item.getProduto().getId().equals(produtoId)) {
                    ajustesSaida += item.getQtd();
                }
            }
        }

        return vendas + transferencias + ajustesSaida;
    }

    /**
     * Retorna o saldo dinâmico em tempo real de um produto específico em um estabelecimento
     */
    public Long calcularSaldoProduto(Integer estabelecimentoId, Integer produtoId) {
        return Math.round(calcularSaldoDisponivel(produtoId, estabelecimentoId));
    }

    /**
     * Retorna o balanço completo de estoque (lista de todos os produtos e seus saldos)
     */
    public java.util.Map<Integer, Long> calcularInventarioCompleto(Integer estabelecimentoId) {
        java.util.Map<Integer, Long> inventario = new java.util.HashMap<>();
        List<Movimentacao> movimentacoes = movimentacaoRepository.findByEstabelecimentoOrigemId(estabelecimentoId);
        movimentacoes.addAll(movimentacaoRepository.findByEstabelecimentoDestinoId(estabelecimentoId));
        
        for (Movimentacao m : movimentacoes) {
            List<ItemMovimentacao> itens = itemRepository.findByMovimentacaoId(m.getId());
            for (ItemMovimentacao item : itens) {
                Integer produtoId = item.getProduto().getId();
                if (!inventario.containsKey(produtoId)) {
                    double saldo = calcularSaldoDisponivel(produtoId, estabelecimentoId);
                    inventario.put(produtoId, Math.round(saldo));
                }
            }
        }
        return inventario;
    }

    /**
     * Lista o extrato/histórico de entradas e saídas que compuseram o saldo do produto na loja
     */
    public java.util.Map<String, Object> obterExtratoMovimentacoes(Integer estabelecimentoId, Integer produtoId) {
        List<Movimentacao> movimentacoes = obterExtrato(produtoId, estabelecimentoId);
        
        java.util.Map<String, Object> extrato = new java.util.HashMap<>();
        extrato.put("estabelecimentoId", estabelecimentoId);
        extrato.put("produtoId", produtoId);
        extrato.put("saldoAtual", calcularSaldoProduto(estabelecimentoId, produtoId));
        
        List<java.util.Map<String, Object>> movimentacoesList = new java.util.ArrayList<>();
        for (Movimentacao mov : movimentacoes) {
            java.util.Map<String, Object> movMap = new java.util.HashMap<>();
            movMap.put("id", mov.getId());
            movMap.put("tipo", mov.getTipoMovimentacao());
            movMap.put("status", mov.getStatus());
            movMap.put("dataHora", mov.getDataHora());
            
            List<ItemMovimentacao> itens = itemRepository.findByMovimentacaoId(mov.getId());
            double quantidade = 0;
            for (ItemMovimentacao item : itens) {
                if (item.getProduto().getId().equals(produtoId)) {
                    quantidade += item.getQtd();
                }
            }
            movMap.put("quantidade", quantidade);
            movMap.put("observacao", mov.getObservacao());
            
            movimentacoesList.add(movMap);
        }
        extrato.put("movimentacoes", movimentacoesList);
        
        return extrato;
    }
}
