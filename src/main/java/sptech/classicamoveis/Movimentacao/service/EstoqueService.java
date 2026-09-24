package sptech.classicamoveis.Movimentacao.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sptech.classicamoveis.Colaborador.model.Colaborador;
import sptech.classicamoveis.Colaborador.repository.ColaboradorRepository;
import sptech.classicamoveis.Estabelecimento.repository.EstabelecimentoRepository;
import sptech.classicamoveis.Movimentacao.dto.InventarioContagemItemDto;

import java.util.ArrayList;
import java.util.List;

import sptech.classicamoveis.Movimentacao.Movimentacao;
import sptech.classicamoveis.Movimentacao.ItemMovimentacao.ItemMovimentacao;
import sptech.classicamoveis.Movimentacao.ItemMovimentacao.ItemMovimentacaoRepository;
import sptech.classicamoveis.Movimentacao.TipoMovimentacao.TipoMovimentacao;
import sptech.classicamoveis.Movimentacao.StatusMovimentacao.StatusMovimentacao;
import sptech.classicamoveis.Movimentacao.MovimentacaoRepository;

import sptech.classicamoveis.Movimentacao.dto.InventarioProdutoDto;

import sptech.classicamoveis.Produto.mapper.ProdutoMapper;
import sptech.classicamoveis.Movimentacao.dto.ProdutoEstoqueResponseDto;
import sptech.classicamoveis.Produto.model.Produto;
import sptech.classicamoveis.Produto.repository.ProdutoRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EstoqueService {

    private final MovimentacaoRepository movimentacaoRepository;
    private final ItemMovimentacaoRepository itemRepository;
    private final ProdutoRepository produtoRepository;
    private final ProdutoMapper produtoMapper;
    private final ColaboradorRepository colaboradorRepository;
    private final EstabelecimentoRepository estabelecimentoRepository;


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
        // VENDA PENDENTE: já reserva o estoque
        List<Movimentacao> vendasPendentes =
                movimentacaoRepository
                        .findByTipoMovimentacaoAndStatusAndEstabelecimentoOrigemId(
                                TipoMovimentacao.VENDA,
                                StatusMovimentacao.PENDENTE,
                                estabelecimentoId
                        );

        for (Movimentacao m : vendasPendentes) {

            List<ItemMovimentacao> itens =
                    itemRepository.findByMovimentacaoId(m.getId());

            for (ItemMovimentacao item : itens) {

                if (item.getProduto().getId().equals(produtoId)) {
                    vendas += item.getQtd();
                }
            }
        }


// VENDA CONCLUIDA: também já saiu do estoque
        List<Movimentacao> vendasConcluidas =
                movimentacaoRepository
                        .findByTipoMovimentacaoAndStatusAndEstabelecimentoOrigemId(
                                TipoMovimentacao.VENDA,
                                StatusMovimentacao.CONCLUIDO,
                                estabelecimentoId
                        );

        for (Movimentacao m : vendasConcluidas) {

            List<ItemMovimentacao> itens =
                    itemRepository.findByMovimentacaoId(m.getId());

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

    @Transactional
    public List<InventarioProdutoDto> registrarContagemLote(
            Integer estabelecimentoId,
            Integer colaboradorId,
            List<InventarioContagemItemDto> itens) {

        if (!estabelecimentoRepository.existsById(estabelecimentoId)) {

            throw new EntityNotFoundException(
                    "Estabelecimento não encontrado"
            );
        }

        List<InventarioProdutoDto> resultados =
                new ArrayList<>();

        for (InventarioContagemItemDto item : itens) {

            InventarioProdutoDto resultado =
                    registrarContagem(
                            estabelecimentoId,
                            item.produtoId(),
                            item.quantidadeContada(),
                            colaboradorId
                    );

            resultados.add(resultado);
        }

        return resultados;
    }


    /**
     * Busca todos os produtos ativos para montar a tela de inventário.
     */
    public List<InventarioProdutoDto> buscarInventario(
            Integer estabelecimentoId) {

        if (!estabelecimentoRepository.existsById(estabelecimentoId)) {
            throw new EntityNotFoundException(
                    "Estabelecimento não encontrado"
            );
        }

        List<InventarioProdutoDto> inventario =
                new java.util.ArrayList<>();

        List<Produto> produtos =
                produtoRepository.findByAtivoTrue();

        for (Produto produto : produtos) {

            Long quantidadeAtual =
                    calcularSaldoProduto(
                            estabelecimentoId,
                            produto.getId()
                    );

            InventarioProdutoDto dto =
                    new InventarioProdutoDto(
                            produto.getId(),
                            produto.getNome(),
                            produto.getMarca(),
                            produto.getFornecedor().getNome(),
                            produto.getCategoria().getCategoria(),
                            quantidadeAtual,
                            null
                    );

            inventario.add(dto);
        }

        return inventario;
    }

    /**
     * Registra a quantidade encontrada no inventário físico.
     *
     * Se a quantidade física for diferente do estoque atual,
     * cria automaticamente um ajuste de entrada ou saída.
     */
    @Transactional
    public InventarioProdutoDto registrarContagem(
            Integer estabelecimentoId,
            Integer produtoId,
            Integer quantidadeContada,
            Integer colaboradorId) {

        if (quantidadeContada == null || quantidadeContada < 0) {

            throw new IllegalArgumentException(
                    "A quantidade contada deve ser maior ou igual a zero"
            );
        }

        if (!estabelecimentoRepository.existsById(estabelecimentoId)) {

            throw new EntityNotFoundException(
                    "Estabelecimento não encontrado"
            );
        }

        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Produto não encontrado"
                        )
                );

        if (!produto.getAtivo()) {

            throw new IllegalArgumentException(
                    "Não é possível realizar inventário de um produto inativo"
            );
        }

        Colaborador colaborador =
                colaboradorRepository.findById(colaboradorId)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Colaborador não encontrado"
                                )
                        );

        Long quantidadeAtual =
                calcularSaldoProduto(
                        estabelecimentoId,
                        produtoId
                );

        int diferenca =
                quantidadeContada - quantidadeAtual.intValue();

        if (diferenca != 0) {

            Movimentacao movimentacao =
                    new Movimentacao();

            movimentacao.setDataHora(
                    java.time.LocalDateTime.now()
            );

            movimentacao.setStatus(
                    StatusMovimentacao.CONCLUIDO
            );

            movimentacao.setColaborador(
                    colaborador
            );

            movimentacao.setEstabelecimentoOrigem(
                    estabelecimentoRepository.findById(
                            estabelecimentoId
                    ).orElseThrow(() ->
                            new EntityNotFoundException(
                                    "Estabelecimento não encontrado"
                            )
                    )
            );

            movimentacao.setValorTotal(0.0);

            movimentacao.setObservacao(
                    "Ajuste realizado pelo inventário físico. " +
                            "Estoque anterior: " + quantidadeAtual +
                            ". Quantidade contada: " + quantidadeContada +
                            ". Diferença: " + diferenca
            );

            if (diferenca > 0) {

                movimentacao.setTipoMovimentacao(
                        TipoMovimentacao.AJUSTE_ENTRADA
                );

            } else {

                movimentacao.setTipoMovimentacao(
                        TipoMovimentacao.AJUSTE_SAIDA
                );
            }

            Movimentacao movimentacaoSalva =
                    movimentacaoRepository.save(
                            movimentacao
                    );

            ItemMovimentacao item =
                    new ItemMovimentacao();

            item.setMovimentacao(
                    movimentacaoSalva
            );

            item.setProduto(
                    produto
            );

            item.setQtd(
                    Math.abs(diferenca)
            );

            item.setPrecoUnitario(0.0);

            item.setDesconto(0.0);

            itemRepository.save(item);
        }

        Long novoSaldo =
                calcularSaldoProduto(
                        estabelecimentoId,
                        produtoId
                );

        return new InventarioProdutoDto(
                produto.getId(),
                produto.getNome(),
                produto.getMarca(),
                produto.getFornecedor().getNome(),
                produto.getCategoria().getCategoria(),
                novoSaldo,
                quantidadeContada
        );
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

    public List<ProdutoEstoqueResponseDto> listarProdutosComSaldo(Integer estabelecimentoId) {
        List<Produto> produtos = produtoRepository.findAll();
        List<ProdutoEstoqueResponseDto> resposta = new ArrayList<>();

        for (Produto produto : produtos) {
            if (produto.getAtivo() != null && !produto.getAtivo()) {
                continue;
            }

            Long saldoDisponivel = estabelecimentoId == null
                    ? calcularSaldoGeralProduto(produto.getId())
                    : calcularSaldoProduto(estabelecimentoId, produto.getId());

            ProdutoEstoqueResponseDto dto = new ProdutoEstoqueResponseDto(
                    produto.getId(),
                    produto.getNome(),
                    produto.getSku(),
                    saldoDisponivel,
                    produto.getEstoqueMinimo(),
                    produto.getFornecedor().getNome()
            );
            resposta.add(dto);
        }

        return resposta;
    }

    public Long calcularSaldoGeralProduto(Integer produtoId) {
        double entradas = 0;
        double saidas = 0;

        List<Movimentacao> movimentacoes = movimentacaoRepository.findAll();
        for (Movimentacao movimentacao : movimentacoes) {
            if (!StatusMovimentacao.CONCLUIDO.equals(movimentacao.getStatus())) {
                continue;
            }

            List<ItemMovimentacao> itens = itemRepository.findByMovimentacaoId(movimentacao.getId());
            for (ItemMovimentacao item : itens) {
                if (!item.getProduto().getId().equals(produtoId)) {
                    continue;
                }

                if (movimentacao.getTipoMovimentacao() == TipoMovimentacao.COMPRA
                        || movimentacao.getTipoMovimentacao() == TipoMovimentacao.TRANSFERENCIA && movimentacao.getEstabelecimentoDestino() != null
                        || movimentacao.getTipoMovimentacao() == TipoMovimentacao.AJUSTE_ENTRADA) {
                    entradas += item.getQtd();
                }

                if (movimentacao.getTipoMovimentacao() == TipoMovimentacao.VENDA
                        || movimentacao.getTipoMovimentacao() == TipoMovimentacao.TRANSFERENCIA && movimentacao.getEstabelecimentoOrigem() != null
                        || movimentacao.getTipoMovimentacao() == TipoMovimentacao.AJUSTE_SAIDA) {
                    saidas += item.getQtd();
                }
            }
        }

        return Math.round(entradas - saidas);
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
