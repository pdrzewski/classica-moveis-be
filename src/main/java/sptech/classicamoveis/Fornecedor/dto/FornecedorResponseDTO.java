package sptech.classicamoveis.Fornecedor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import sptech.classicamoveis.Endereco.dto.EnderecoResponseDTO;

@Schema(description = "Dados de um fornecedor retornados pela API")
public record FornecedorResponseDTO(
        @Schema(example = "4") Long id,
        @Schema(example = "Rufato Estofados Ltda") String nome,
        @Schema(example = "12345678000199") String cnpj,
        @Schema(example = "Carlos Rufato") String representante,
        @Schema(example = "11912345678") String telefone1,
        @Schema(example = "1140028922") String telefone2,
        EnderecoResponseDTO endereco
) {
}
