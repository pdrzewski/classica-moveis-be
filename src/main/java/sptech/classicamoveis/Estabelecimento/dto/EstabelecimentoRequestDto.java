package sptech.classicamoveis.Estabelecimento.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EstabelecimentoRequestDto {
    private String nome;
    private Integer enderecoId;
    private String cnpj;
    private String telefone;
    private Integer responsavelId;

    public String getNome() { return nome; }
}
