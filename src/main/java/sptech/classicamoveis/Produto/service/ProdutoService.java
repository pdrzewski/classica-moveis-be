package sptech.classicamoveis.Produto.service;

import sptech.classicamoveis.Compra.Compra;
import sptech.classicamoveis.Movimentacao.ItemMovimentacao.ItemMovimentacao;
import sptech.classicamoveis.Movimentacao.Movimentacao;
import sptech.classicamoveis.Produto.dto.ProdutoRequestDTO;
import sptech.classicamoveis.Produto.dto.ProdutoResponseDTO;
import sptech.classicamoveis.Transferencia.Transferencia;
import sptech.classicamoveis.Venda.Venda;

import java.util.List;

public interface ProdutoService {

    List<ProdutoResponseDTO> listarTodos();

    ProdutoResponseDTO buscarPorId(Integer id);

    ProdutoResponseDTO criar(ProdutoRequestDTO dto);

    ProdutoResponseDTO atualizar(Integer id, ProdutoRequestDTO dto);

    void deletar(Integer id);

    List<Movimentacao> listarTodasMovimentacoes();

    Venda registrarVenda(Venda venda, List<ItemMovimentacao> itens);
    Compra registrarCompra(Compra compra, List<ItemMovimentacao> itens);
    Transferencia registrarTransferencia(Transferencia transferencia, List<ItemMovimentacao> itens);
}
