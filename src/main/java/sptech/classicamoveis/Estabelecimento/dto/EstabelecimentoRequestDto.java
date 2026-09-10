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
@Schema(description = "Dados para atualização de estabelecimento (endereço já cadastrado)")
public class EstabelecimentoRequestDto {
    @Schema(example = "Classica Móveis - Matriz Tatuapé")
    private String nome;
    @Schema(example = "9")
    private Integer enderecoId;
    @Schema(example = "98765432000110")
    private String cnpj;
    @Schema(example = "1123456789")
    private String telefone;
    @Schema(example = "1")
    private Integer responsavelId;

    public String getNome() { return nome; }
}
