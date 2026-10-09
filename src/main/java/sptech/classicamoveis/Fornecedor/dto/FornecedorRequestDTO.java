package sptech.classicamoveis.Fornecedor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record FornecedorRequestDTO(
        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 45, message = "Nome deve ter no máximo 45 caracteres")
        @Schema(example = "Rufato Estofados Ltda")
        String nome,

        @NotBlank(message = "CNPJ é obrigatório")
        @Size(max = 14, message = "CNPJ deve ter no máximo 14 caracteres")
        @Schema(example = "12345678000199")
        String cnpj,

        @Size(max = 11, message = "Telefone1 deve ter no máximo 11 caracteres")
        @Schema(example = "11912345678")
        String telefone1,

        @Size(max = 11, message = "Telefone2 deve ter no máximo 11 caracteres")
        @Schema(example = "1140028922")
        String telefone2,

        @Size(max = 11, message = "WhatsApp deve ter no máximo 11 caracteres")
        @Schema(example = "11987654321")
        String whatsapp,

        @Email(message = "E-mail inválido")
        @Size(max = 100, message = "E-mail deve ter no máximo 100 caracteres")
        @Schema(example = "contato@rufato.com.br")
        String email,

        @Schema(example = "22", description = "ID do endereço existente; opcional quando enviados os campos do endereço")
        Integer enderecoId,

        @Size(max = 9, message = "CEP deve ter no máximo 9 caracteres") String cep,
        @Size(max = 100, message = "Logradouro deve ter no máximo 100 caracteres") String logradouro,
        @Size(max = 45, message = "Bairro deve ter no máximo 45 caracteres") String bairro,
        @Size(max = 45, message = "Cidade deve ter no máximo 45 caracteres") String cidade,
        @Size(max = 45, message = "Número deve ter no máximo 45 caracteres") String numero,
        @Size(max = 45, message = "Complemento deve ter no máximo 45 caracteres") String complemento,
        @Size(max = 2, message = "Estado deve ter 2 caracteres") String estado

) {
}
