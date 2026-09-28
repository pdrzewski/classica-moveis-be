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

        @NotNull(message = "O id do endereço é obrigatório")
        @Schema(example = "22")
        Integer enderecoId

) {
        // Compatibilidade com o contrato anterior, que possuía o campo representante.
        public FornecedorRequestDTO(String nome, String cnpj, String representante,
                                    String telefone1, String telefone2, Integer enderecoId) {
                this(nome, cnpj, telefone1, telefone2, null, null, enderecoId);
        }
}
