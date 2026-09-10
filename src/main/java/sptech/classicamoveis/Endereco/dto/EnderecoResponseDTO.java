package sptech.classicamoveis.Endereco.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados de endereço retornados pela API")
public record EnderecoResponseDTO(
        @Schema(example = "22") Integer id,
        @Schema(example = "04578-000") String cep,
        @Schema(example = "Rua Industrial") String logradouro,
        @Schema(example = "Vila Industrial") String bairro,
        @Schema(example = "Guarulhos") String cidade,
        @Schema(example = "540") String numero,
        @Schema(example = "Galpão 3") String complemento,
        @Schema(example = "SP") String estado
) {
}
