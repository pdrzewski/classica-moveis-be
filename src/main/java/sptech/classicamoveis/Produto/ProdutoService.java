package sptech.classicamoveis.Produto;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sptech.classicamoveis.Compra.Compra;
import sptech.classicamoveis.Compra.CompraRepository;
import sptech.classicamoveis.Movimentacao.ItemMovimentacao.ItemMovimentacao;
import sptech.classicamoveis.Movimentacao.ItemMovimentacao.ItemMovimentacaoRepository;
import sptech.classicamoveis.Movimentacao.Movimentacao;
import sptech.classicamoveis.Movimentacao.MovimentacaoRepository;
import sptech.classicamoveis.Movimentacao.TipoMovimentacao.TipoMovimentacao;
import sptech.classicamoveis.Transferencia.Transferencia;
import sptech.classicamoveis.Transferencia.TransferenciaRepository;
import sptech.classicamoveis.Venda.Venda;
import sptech.classicamoveis.Venda.VendaRepository;

import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final MovimentacaoRepository movimentacaoRepository;
    private final ItemMovimentacaoRepository itemMovimentacaoRepository;
    private final TransferenciaRepository transferenciaRepository;
    private final VendaRepository vendaRepository;
    private final CompraRepository compraRepository;

    public ProdutoService(ProdutoRepository produtoRepository,
                          MovimentacaoRepository movimentacaoRepository,
                          ItemMovimentacaoRepository itemMovimentacaoRepository,
                          TransferenciaRepository transferenciaRepository,
                          VendaRepository vendaRepository,
                          CompraRepository compraRepository) {
        this.produtoRepository = produtoRepository;
        this.movimentacaoRepository = movimentacaoRepository;
        this.itemMovimentacaoRepository = itemMovimentacaoRepository;
        this.transferenciaRepository = transferenciaRepository;
        this.vendaRepository = vendaRepository;
        this.compraRepository = compraRepository;
    }


    public List<Produto> listarTodosProdutos() {
        return produtoRepository.findAll();
    }

    public Produto buscarProdutoPorId(Integer id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com ID: " + id));
    }

    public Produto salvarProduto(Produto produto) {
        return produtoRepository.save(produto);
    }

    public void deletarProduto(Integer id) {
        produtoRepository.deleteById(id);
    }



    public List<Movimentacao> listarTodasMovimentacoes() {
        return movimentacaoRepository.findAll();
    }



    private void prepararEPreencherItens(Movimentacao movimentacao, List<ItemMovimentacao> itens) {
        if (itens == null || itens.isEmpty()) return;

        for (ItemMovimentacao item : itens) {
            Produto produto = produtoRepository.findById(item.getProduto().getId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado ID: " + item.getProduto().getId()));

            item.setMovimentacao(movimentacao);
            item.setProduto(produto);

            if (item.getPrecoUnitario() == null) {
                if (movimentacao.getTipoMovimentacao() == TipoMovimentacao.COMPRA) {
                    item.setPrecoUnitario(produto.getPrecoCusto());
                } else {
                    item.setPrecoUnitario(produto.getPrecoVenda());
                }
            }
        }
        itemMovimentacaoRepository.saveAll(itens);
    }

    @Transactional
    public Venda registrarVenda(Venda venda, List<ItemMovimentacao> itens) {
        if (venda.getMovimentacao().getId() == null) {
            venda.getMovimentacao().setTipoMovimentacao(TipoMovimentacao.VENDA);
            movimentacaoRepository.save(venda.getMovimentacao());
        }
        Venda vendaSalva = vendaRepository.save(venda);
        prepararEPreencherItens(venda.getMovimentacao(), itens);
        return vendaSalva;
    }

    @Transactional
    public Compra registrarCompra(Compra compra, List<ItemMovimentacao> itens) {
        if (compra.getMovimentacao().getId() == null) {
            compra.getMovimentacao().setTipoMovimentacao(TipoMovimentacao.COMPRA);
            movimentacaoRepository.save(compra.getMovimentacao());
        }
        Compra compraSalva = compraRepository.save(compra);
        prepararEPreencherItens(compra.getMovimentacao(), itens);
        return compraSalva;
    }

    @Transactional
    public Transferencia registrarTransferencia(Transferencia transferencia, List<ItemMovimentacao> itens) {
        if (transferencia.getMovimentacao().getId() == null) {
            transferencia.getMovimentacao().setTipoMovimentacao(TipoMovimentacao.TRANSFERENCIA);
            movimentacaoRepository.save(transferencia.getMovimentacao());
        }
        Transferencia transfSalva = transferenciaRepository.save(transferencia);
        prepararEPreencherItens(transferencia.getMovimentacao(), itens);
        return transfSalva;
    }

    public List<Produto> buscarProdutos(String nome, Double precoMax, Integer categoriaId) {
        return produtoRepository.buscarComFiltros(nome, precoMax, categoriaId);
    }
}