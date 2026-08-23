package sptech.classicamoveis.Fornecedor.service;

import sptech.classicamoveis.Fornecedor.dto.FornecedorRequestDTO;
import sptech.classicamoveis.Fornecedor.dto.FornecedorComEnderecoRequestDTO;
import sptech.classicamoveis.Fornecedor.dto.FornecedorResponseDTO;

import java.util.List;

public interface FornecedorService {

    List<FornecedorResponseDTO> listarTodos();

    FornecedorResponseDTO buscarPorId(Long id);

    FornecedorResponseDTO criar(FornecedorComEnderecoRequestDTO dto);

    FornecedorResponseDTO atualizar(Long id, FornecedorRequestDTO dto);

    void deletar(Long id);
}