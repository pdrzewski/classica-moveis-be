package sptech.classicamoveis.Fornecedor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import sptech.classicamoveis.Endereco.dto.EnderecoResponseDTO;

public record FornecedorResponseDTO(
        @Schema(example = "4") Long id,
        @Schema(example = "Rufato Estofados Ltda") String nome,
        @Schema(example = "12345678000199") String cnpj,
        @Schema(example = "11912345678") String telefone1,
        @Schema(example = "1140028922") String telefone2,
        @Schema(example = "11987654321") String whatsapp,
        @Schema(example = "contato@rufato.com.br") String email,
        EnderecoResponseDTO endereco

) {
}
