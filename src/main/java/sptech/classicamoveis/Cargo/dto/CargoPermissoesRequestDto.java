package sptech.classicamoveis.Cargo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

@Schema(description = "Permissões vinculadas a um cargo")
public class CargoPermissoesRequestDto {

    @Schema(description = "Ids das permissões vinculadas ao cargo", example = "[1, 2, 4]")
    private Set<Integer> permissoesIds;

    public CargoPermissoesRequestDto() {
    }

    public CargoPermissoesRequestDto(Set<Integer> permissoesIds) {
        this.permissoesIds = permissoesIds;
    }

    public Set<Integer> getPermissoesIds() {
        return permissoesIds;
    }

    public void setPermissoesIds(Set<Integer> permissoesIds) {
        this.permissoesIds = permissoesIds;
    }
}
