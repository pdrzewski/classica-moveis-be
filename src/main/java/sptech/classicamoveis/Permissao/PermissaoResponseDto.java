package sptech.classicamoveis.Permissao;

public class PermissaoResponseDto {

    private Integer id;
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