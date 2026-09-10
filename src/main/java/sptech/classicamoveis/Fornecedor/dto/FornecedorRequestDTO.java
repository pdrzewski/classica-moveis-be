package sptech.classicamoveis.Fornecedor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para atualização de fornecedor (endereço já cadastrado)")
public record FornecedorRequestDTO(

        @NotBlank(message = "Nome e obrigatorio")
        @Size(max = 45, message = "Nome deve ter no maximo 45 caracteres")
        @Schema(example = "Rufato Estofados Ltda")
        String nome,

        @NotBlank(message = "CNPJ e obrigatorio")
        @Size(max = 14, message = "CNPJ deve ter no maximo 14 caracteres")
        @Schema(example = "12345678000199")
        String cnpj,

        @Size(max = 45, message = "Representante deve ter no maximo 45 caracteres")
        @Schema(example = "Carlos Rufato")
        String representante,

        @Size(max = 11, message = "Telefone1 deve ter no maximo 11 caracteres")
        @Schema(example = "11912345678")
        String telefone1,

        @Size(max = 11, message = "Telefone2 deve ter no maximo 11 caracteres")
        @Schema(example = "1140028922")
        String telefone2,

        @NotNull(message = "O id do endereco e obrigatorio")
        @Schema(example = "22")
        Integer enderecoId
) {

}
