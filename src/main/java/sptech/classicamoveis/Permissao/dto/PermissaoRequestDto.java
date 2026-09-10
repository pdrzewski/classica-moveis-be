package sptech.classicamoveis.Permissao.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para criação/atualização de permissão")
public class PermissaoRequestDto {

    @Schema(example = "CRIAR_VENDA")
    private String nome;

    public PermissaoRequestDto(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}
