package sptech.classicamoveis.Cliente.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Dados de cliente (endereço já cadastrado)")
public class ClienteRequestDto {

    @NotBlank(message = "Nome é obrigatório")
    @Schema(example = "Maria Aparecida Souza")
    private String nome;

    @NotBlank(message = "ID do endereço é obrigatório")
    @Schema(example = "15")
    private Integer enderecoId;

    @NotBlank(message = "Documento é obrigatório")
    @Schema(example = "45678912300")
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

    @Schema(example = "isento")
    private String ie;

}
