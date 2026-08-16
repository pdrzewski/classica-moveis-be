package sptech.classicamoveis.Fornecedor.dto;

import sptech.classicamoveis.Endereco.dto.EnderecoResponseDTO;

public record FornecedorResponseDTO(
        Long id,
        String nome,
        String cnpj,
        String representante,
        String telefone1,
        String telefone2,
        EnderecoResponseDTO endereco
) {
}