package sptech.classicamoveis.Representante.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record RepresentanteRequestDTO(
        @NotBlank(message = "Nome do representante é obrigatório")
        @Size(max = 45, message = "Nome deve ter no máximo 45 caracteres")
        @Schema(example = "Carlos Rufato")
        String nome,

        @Size(max = 20, message = "Telefone1 deve ter no máximo 20 caracteres")
        @Schema(example = "11912345678")
        String telefone1,

        @Size(max = 20, message = "Telefone2 deve ter no máximo 20 caracteres")
        @Schema(example = "1140028922")
        String telefone2,

        @Schema(description = "IDs dos fornecedores/fábricas representados", example = "[1, 2]")
        List<Long> fornecedorIds
) {}
