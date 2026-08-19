package sptech.classicamoveis.Permissao.dto;

public class PermissaoRequestDto {

    private String nome;

    public PermissaoRequestDto(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}