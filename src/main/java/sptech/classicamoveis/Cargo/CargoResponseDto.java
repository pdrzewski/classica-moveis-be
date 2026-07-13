package sptech.classicamoveis.Cargo;

import java.util.Set;

public class CargoResponseDto {

    private Integer id;
    private String nome;
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