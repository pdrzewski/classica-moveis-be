package sptech.classicamoveis.Cargo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

@Schema(description = "Dados de cargo retornados pela API")
public class CargoResponseDto {

    @Schema(example = "2")
    private Integer id;
    @Schema(example = "Vendedor")
    private String nome;
    @Schema(example = "[\"CRIAR_VENDA\", \"CONSULTAR_ESTOQUE\"]")
    private Set<String> permissoes;

    public CargoResponseDto(Integer id, String nome, Set<String> permissoes) {
        this.id = id;
        this.nome = nome;
        this.permissoes = permissoes;
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Set<String> getPermissoes() {
        return permissoes;
    }
}
