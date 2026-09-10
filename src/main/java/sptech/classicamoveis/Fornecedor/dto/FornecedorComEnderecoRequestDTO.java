package sptech.classicamoveis.Fornecedor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para cadastro de fornecedor já com o endereço")
public record FornecedorComEnderecoRequestDTO(

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

        @NotBlank(message = "CEP é obrigatório")
        @Schema(example = "04578-000")
        String cep,

        @NotBlank(message = "Logradouro é obrigatório")
        @Schema(example = "Rua Industrial")
        String logradouro,

        @NotBlank(message = "Bairro é obrigatório")
        @Schema(example = "Vila Industrial")
        String bairro,

        @NotBlank(message = "Cidade é obrigatório")
        @Schema(example = "Guarulhos")
        String cidade,

        @NotBlank(message = "Número é obrigatório")
        @Schema(example = "540")
        String numero,

        @Schema(example = "Galpão 3")
        String complemento,

        @NotBlank(message = "Estado é obrigatório")
        @Schema(example = "SP")
        String estado
) {
}
