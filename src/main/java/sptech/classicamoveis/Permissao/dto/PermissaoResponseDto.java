package sptech.classicamoveis.Permissao.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados de permissão retornados pela API")
public class PermissaoResponseDto {

    @Schema(example = "1")
    private Integer id;
    @Schema(example = "CRIAR_VENDA")
    private String nome;

    public PermissaoResponseDto(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
}
