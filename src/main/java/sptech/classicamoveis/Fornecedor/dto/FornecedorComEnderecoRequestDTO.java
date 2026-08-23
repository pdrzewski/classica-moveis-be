package sptech.classicamoveis.Fornecedor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FornecedorComEnderecoRequestDTO(

        @NotBlank(message = "Nome e obrigatorio")
        @Size(max = 45, message = "Nome deve ter no maximo 45 caracteres")
        String nome,

        @NotBlank(message = "CNPJ e obrigatorio")
        @Size(max = 14, message = "CNPJ deve ter no maximo 14 caracteres")
        String cnpj,

        @Size(max = 45, message = "Representante deve ter no maximo 45 caracteres")
        String representante,

        @Size(max = 11, message = "Telefone1 deve ter no maximo 11 caracteres")
        String telefone1,

        @Size(max = 11, message = "Telefone2 deve ter no maximo 11 caracteres")
        String telefone2,

        @NotBlank(message = "CEP é obrigatório")
        String cep,

        @NotBlank(message = "Logradouro é obrigatório")
        String logradouro,

        @NotBlank(message = "Bairro é obrigatório")
        String bairro,

        @NotBlank(message = "Cidade é obrigatório")
        String cidade,

        @NotBlank(message = "Número é obrigatório")
        String numero,

        String complemento,

        @NotBlank(message = "Estado é obrigatório")
        String estado
) {
}
