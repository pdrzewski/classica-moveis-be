package sptech.classicamoveis.Cliente.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Schema(description = "Dados para cadastro de cliente já com o endereço")
public class ClienteComEnderecoRequestDto {

    @NotBlank(message = "Nome é obrigatório")
    @Schema(example = "Maria Aparecida Souza")
    private String nome;

    @NotBlank(message = "Documento é obrigatório")
    @Schema(description = "CPF ou CNPJ do cliente", example = "45678912300")
    private String documento;

    @NotBlank(message = "Telefone 1 é obrigatório")
    @Schema(example = "11987654321")
    private String telefone1;

    @NotBlank(message = "Telefone 2 é obrigatório")
    @Schema(example = "1133224455")
    private String telefone2;

    @NotBlank(message = "Email é obrigatório")
    @Schema(example = "maria.souza@email.com")
    private String email;

    @Schema(example = "Cliente prefere entrega no período da tarde")
    private String observacao;

    @Schema(description = "Inscrição estadual (quando cliente for pessoa jurídica)", example = "isento")
    private String ie;

    // Dados do Endereço
    @NotBlank(message = "CEP é obrigatório")
    @Schema(example = "01310-100")
    private String cep;

    @NotBlank(message = "Logradouro é obrigatório")
    @Schema(example = "Avenida Paulista")
    private String logradouro;

    @NotBlank(message = "Bairro é obrigatório")
    @Schema(example = "Bela Vista")
    private String bairro;

    @NotBlank(message = "Cidade é obrigatório")
    @Schema(example = "São Paulo")
    private String cidade;

    @NotBlank(message = "Número é obrigatório")
    @Schema(example = "1578")
    private String numero;

    @Schema(example = "Apto 92")
    private String complemento;

    @NotBlank(message = "Estado é obrigatório")
    @Schema(example = "SP")
    private String estado;
}
