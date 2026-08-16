package sptech.classicamoveis.Produto.service;

import sptech.classicamoveis.Produto.dto.ProdutoRequestDTO;
import sptech.classicamoveis.Produto.dto.ProdutoResponseDTO;

import java.util.List;

public interface ProdutoService {

    List<ProdutoResponseDTO> listarTodos();

    ProdutoResponseDTO buscarPorId(Integer id);

    ProdutoResponseDTO criar(ProdutoRequestDTO dto);

    ProdutoResponseDTO atualizar(Integer id, ProdutoRequestDTO dto);

    void deletar(Integer id);
}
