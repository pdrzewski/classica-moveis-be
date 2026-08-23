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
    private Integer enderecoId;
    private String documento;
    private String telefone1;
    private String telefone2;
    private String email;
    private String observacao;
    private String ie;

}
