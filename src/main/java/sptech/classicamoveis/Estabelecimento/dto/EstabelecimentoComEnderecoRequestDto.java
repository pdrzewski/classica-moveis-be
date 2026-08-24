package sptech.classicamoveis.Estabelecimento.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EstabelecimentoComEnderecoRequestDto {
    private String nome;
    private String cnpj;
    private String telefone;
    private Integer responsavelId;

    // Dados do Endereço
    private String cep;
    private String logradouro;
    private String bairro;
    private String cidade;
    private String numero;
    private String complemento;
    private String estado;
}
