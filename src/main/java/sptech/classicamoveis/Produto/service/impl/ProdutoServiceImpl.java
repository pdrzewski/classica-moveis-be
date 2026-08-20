package sptech.classicamoveis.Produto.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sptech.classicamoveis.Categoria.Categoria;
import sptech.classicamoveis.Categoria.CategoriaRepository;
import sptech.classicamoveis.Compra.Compra;
import sptech.classicamoveis.Compra.CompraRepository;
import sptech.classicamoveis.Fornecedor.model.Fornecedor;
import sptech.classicamoveis.Fornecedor.repository.FornecedorRepository;
import sptech.classicamoveis.Movimentacao.ItemMovimentacao.ItemMovimentacao;
import sptech.classicamoveis.Movimentacao.ItemMovimentacao.ItemMovimentacaoRepository;
import sptech.classicamoveis.Movimentacao.Movimentacao;
import sptech.classicamoveis.Movimentacao.MovimentacaoRepository;
import sptech.classicamoveis.Movimentacao.TipoMovimentacao.TipoMovimentacao;
import sptech.classicamoveis.Produto.dto.ProdutoRequestDTO;
import sptech.classicamoveis.Produto.dto.ProdutoResponseDTO;
import sptech.classicamoveis.Produto.exception.RecursoNaoEncontradoException;
import sptech.classicamoveis.Produto.mapper.ProdutoMapper;
import sptech.classicamoveis.Produto.model.Produto;
import sptech.classicamoveis.Produto.repository.ProdutoRepository;
import sptech.classicamoveis.Produto.service.ProdutoService;
import sptech.classicamoveis.Transferencia.model.Transferencia;
import sptech.classicamoveis.Transferencia.repository.TransferenciaRepository;
import sptech.classicamoveis.Venda.model.Venda;
import sptech.classicamoveis.Venda.repository.VendaRepository;

import java.util.List;
import java.util.Set;
import java.util.LinkedHashSet;

@Service
@RequiredArgsConstructor
@Transactional
public class ProdutoServiceImpl implements ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final FornecedorRepository fornecedorRepository;
    private final CategoriaRepository categoriaRepository;
    private final VendaRepository vendaRepository;
    private final CompraRepository compraRepository;
    private final TransferenciaRepository transferenciaRepository;
    private final MovimentacaoRepository movimentacaoRepository;
    private final ItemMovimentacaoRepository itemMovimentacaoRepository;
    private final ProdutoMapper produtoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> listarTodos() {
        return produtoRepository.findAll().stream()
                .map(produtoMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> buscarPorTermo(String termo) {
        if (termo == null || termo.isBlank()) {
            return listarTodos();
        }

        Set<Produto> encontrados = new LinkedHashSet<>();

        // busca textual (nome, fornecedor, categoria, sku, codigoBarras, marca, unidadeMedida)
        encontrados.addAll(produtoRepository.searchByTerm(termo));

        // busca por inteiros (id, estoqueMinimo)
        try {
            Integer inteiro = Integer.valueOf(termo);
            encontrados.addAll(produtoRepository.findByIdEqualsOrEstoqueMinimoEquals(inteiro, inteiro));
        } catch (NumberFormatException ignored) {
        }

        // busca por valores numéricos (precoCusto, precoVenda)
        try {
            Double valor = Double.valueOf(termo.replace(',', '.'));
            encontrados.addAll(produtoRepository.findByPrecoCustoEqualsOrPrecoVendaEquals(valor, valor));
        } catch (NumberFormatException ignored) {
        }

        return encontrados.stream()
                .map(produtoMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProdutoResponseDTO buscarPorId(Integer id) {
        return produtoMapper.toResponseDTO(buscarEntidadePorId(id));
    }

    @Override
    public ProdutoResponseDTO criar(ProdutoRequestDTO dto) {
        Fornecedor fornecedor = buscarFornecedorPorId(dto.fornecedorId());
        Categoria categoria = buscarCategoriaPorId(dto.categoriaId());
        Produto produto = produtoMapper.toEntity(dto, fornecedor, categoria);
        return produtoMapper.toResponseDTO(produtoRepository.save(produto));
    }

    @Override
    public ProdutoResponseDTO atualizar(Integer id, ProdutoRequestDTO dto) {
        Produto produto = buscarEntidadePorId(id);
        Fornecedor fornecedor = buscarFornecedorPorId(dto.fornecedorId());
        Categoria categoria = buscarCategoriaPorId(dto.categoriaId());
        produtoMapper.preencherEntidade(produto, dto, fornecedor, categoria);
        return produtoMapper.toResponseDTO(produtoRepository.save(produto));
    }

    @Override
    public void deletar(Integer id) {
        produtoRepository.delete(buscarEntidadePorId(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Movimentacao> listarTodasMovimentacoes() {
        return movimentacaoRepository.findAll();
    }

    @Override
    public Venda registrarVenda(Venda venda, List<ItemMovimentacao> itens) {
        // Criar e salvar a Movimentacao
        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setTipoMovimentacao(TipoMovimentacao.VENDA);
        Movimentacao movimentacaoSalva = movimentacaoRepository.save(movimentacao);

        // Associar Movimentacao à Venda
        venda.setMovimentacao(movimentacaoSalva);
        Venda vendaSalva = vendaRepository.save(venda);

        // Associar Movimentacao aos itens
        itens.forEach(item -> item.setMovimentacao(movimentacaoSalva));
        itemMovimentacaoRepository.saveAll(itens);

        return vendaSalva;
    }

    @Override
    public Compra registrarCompra(Compra compra, List<ItemMovimentacao> itens) {
        // Criar e salvar a Movimentacao
        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setTipoMovimentacao(TipoMovimentacao.COMPRA);
        Movimentacao movimentacaoSalva = movimentacaoRepository.save(movimentacao);

        // Associar Movimentacao à Compra
        compra.setMovimentacao(movimentacaoSalva);
        Compra compraSalva = compraRepository.save(compra);

        // Associar Movimentacao aos itens
        itens.forEach(item -> item.setMovimentacao(movimentacaoSalva));
        itemMovimentacaoRepository.saveAll(itens);

        return compraSalva;
    }

    @Override
    public Transferencia registrarTransferencia(Transferencia transferencia, List<ItemMovimentacao> itens) {
        // Criar e salvar a Movimentacao
        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setTipoMovimentacao(TipoMovimentacao.TRANSFERENCIA);
        Movimentacao movimentacaoSalva = movimentacaoRepository.save(movimentacao);

        // Associar Movimentacao à Transferencia
        transferencia.setMovimentacao(movimentacaoSalva);
        Transferencia transferenciaSalva = transferenciaRepository.save(transferencia);

        // Associar Movimentacao aos itens
        itens.forEach(item -> item.setMovimentacao(movimentacaoSalva));
        itemMovimentacaoRepository.saveAll(itens);

        return transferenciaSalva;
    }

    private Produto buscarEntidadePorId(Integer id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado com id: " + id));
    }

    private Fornecedor buscarFornecedorPorId(Long id) {
        return fornecedorRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Fornecedor não encontrado com id: " + id));
    }

    private Categoria buscarCategoriaPorId(Integer id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria não encontrada com id: " + id));
    }
}