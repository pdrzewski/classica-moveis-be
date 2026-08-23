package sptech.classicamoveis.Cliente.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ClienteResponseDto {

    private Integer id;
    private String nome;
    private String documento;
    private String telefone1;
    private String telefone2;
    private String email;
    private String observacao;
    private String ie;

    // Dados do Endereço
    private Integer enderecoId;
    private String cep;
    private String logradouro;
    private String bairro;
    private String cidade;
    private String numero;
    private String complemento;
    private String estado;

}
