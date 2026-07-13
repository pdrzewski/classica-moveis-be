package sptech.classicamoveis.Cargo;

import java.util.Set;

public class CargoRequestDto {

    private String nome;
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