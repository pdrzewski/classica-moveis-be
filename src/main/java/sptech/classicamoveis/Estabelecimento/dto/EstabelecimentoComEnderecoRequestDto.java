package sptech.classicamoveis.Estabelecimento.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados para cadastro de estabelecimento (loja/filial) já com o endereço")
public class EstabelecimentoComEnderecoRequestDto {
    @Schema(example = "Classica Móveis - Matriz Tatuapé")
    private String nome;
    @Schema(example = "98765432000110")
    private String cnpj;
    @Schema(example = "1123456789")
    private String telefone;
    @Schema(description = "Id do colaborador responsável pelo estabelecimento", example = "1")
    private Integer responsavelId;

    // Dados do Endereço
    @Schema(example = "03310-000")
    private String cep;
    @Schema(example = "Rua Serra de Botucatu")
    private String logradouro;
    @Schema(example = "Tatuapé")
    private String bairro;
    @Schema(example = "São Paulo")
    private String cidade;
    @Schema(example = "1200")
    private String numero;
    @Schema(example = "Loja 2")
    private String complemento;
    @Schema(example = "SP")
    private String estado;
}
