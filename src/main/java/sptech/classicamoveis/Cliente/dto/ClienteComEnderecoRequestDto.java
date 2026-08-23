package sptech.classicamoveis.Cliente.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClienteComEnderecoRequestDto {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

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

    // Dados do Endereço
    @NotBlank(message = "CEP é obrigatório")
    private String cep;

    @NotBlank(message = "Logradouro é obrigatório")
    private String logradouro;

    @NotBlank(message = "Bairro é obrigatório")
    private String bairro;

    @NotBlank(message = "Cidade é obrigatório")
    private String cidade;

    @NotBlank(message = "Número é obrigatório")
    private String numero;

    private String complemento;

    @NotBlank(message = "Estado é obrigatório")
    private String estado;
}
