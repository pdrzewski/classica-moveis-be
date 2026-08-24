package sptech.classicamoveis.Cliente.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class ClienteRequestDto {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "ID do endereço é obrigatório")
    private Integer enderecoId;

    @NotBlank(message = "Documento é obrigatório")
    private String documento;

    @NotBlank(message = "Telefone 1 é obrigatório")
    private String telefone1;

    @NotBlank(message = "Telefone 2 é obrigatório")
    private String telefone2;
    @NotBlank(message = "Email é obrigatório")
    private String email;

    private String observacao;

    private String ie;

}
