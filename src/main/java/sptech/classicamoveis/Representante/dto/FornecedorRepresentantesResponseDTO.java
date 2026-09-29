package sptech.classicamoveis.Representante.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record FornecedorRepresentantesResponseDTO(

        @Schema(example = "1")
        Long id,

        @Schema(example = "Móveis Blumenau Ltda")
        String nome,

        @Schema(example = "12345678000199")
        String cnpj,

        List<RepresentanteDTO> representantes

) {

    public record RepresentanteDTO(

            @Schema(example = "1")
            Long id,

            @Schema(example = "João da Silva")
            String nome,

            @Schema(example = "11999999999")
            String telefone1,

            @Schema(example = "11988888888")
            String telefone2

    ) {
    }
}