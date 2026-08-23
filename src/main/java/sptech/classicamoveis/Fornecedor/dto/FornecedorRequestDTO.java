package sptech.classicamoveis.Fornecedor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record FornecedorRequestDTO(

        @NotBlank(message = "Nome e obrigatorio")
        @Size(max = 45, message = "Nome deve ter no maximo 45 caracteres")
        String nome,

        @NotBlank(message = "CNPJ e obrigatorio")
        @Size(max = 45, message = "CNPJ deve ter no maximo 45 caracteres")
        String cnpj,

        @Size(max = 45, message = "Representante deve ter no maximo 45 caracteres")
        String representante,

        @Size(max = 45, message = "Telefone1 deve ter no maximo 45 caracteres")
        String telefone1,

        @Size(max = 45, message = "Telefone2 deve ter no maximo 45 caracteres")
        String telefone2,

        @NotNull(message = "O id do endereco e obrigatorio")
        Integer enderecoId
) {

}