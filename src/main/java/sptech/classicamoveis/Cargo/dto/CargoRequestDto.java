package sptech.classicamoveis.Cargo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

@Schema(description = "Dados para criação/atualização de cargo")
public class CargoRequestDto {

    @Schema(example = "Vendedor")
    private String nome;

    @Schema(description = "Ids das permissões vinculadas ao cargo", example = "[1, 2, 4]")
    private Set<Integer> permissoesIds;

    public CargoRequestDto(String nome, Set<Integer> permissoesIds) {
        this.nome = nome;
        this.permissoesIds = permissoesIds;
    }

    public String getNome() {
        return nome;
    }

    public Set<Integer> getPermissoesIds() {
        return permissoesIds;
    }
}
