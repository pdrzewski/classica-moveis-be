package sptech.classicamoveis.Representante.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record RepresentanteResponseDTO(
        @Schema(example = "1") Long id,
        @Schema(example = "Carlos Rufato") String nome,
        @Schema(example = "11912345678") String telefone1,
        @Schema(example = "1140028922") String telefone2,
        List<FornecedorRepresentadoDTO> fornecedores
) {
    public record FornecedorRepresentadoDTO(
            @Schema(example = "1") Long id,
            @Schema(example = "Rufato Estofados Ltda") String nome,
            @Schema(example = "12345678000199") String cnpj
    ) {}
}
