package sptech.classicamoveis.Cliente.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Schema(description = "Dados de um cliente retornados pela API")
public class ClienteResponseDto {

    @Schema(example = "8")
    private Integer id;
    @Schema(example = "Maria Aparecida Souza")
    private String nome;
    @Schema(example = "45678912300")
    private String documento;
    @Schema(example = "11987654321")
    private String telefone1;
    @Schema(example = "1133224455")
    private String telefone2;
    @Schema(example = "maria.souza@email.com")
    private String email;
    @Schema(example = "Cliente prefere entrega no período da tarde")
    private String observacao;
    @Schema(example = "isento")
    private String ie;

    // Dados do Endereço
    @Schema(example = "15")
    private Integer enderecoId;
    @Schema(example = "01310-100")
    private String cep;
    @Schema(example = "Avenida Paulista")
    private String logradouro;
    @Schema(example = "Bela Vista")
    private String bairro;
    @Schema(example = "São Paulo")
    private String cidade;
    @Schema(example = "1578")
    private String numero;
    @Schema(example = "Apto 92")
    private String complemento;
    @Schema(example = "SP")
    private String estado;
}
