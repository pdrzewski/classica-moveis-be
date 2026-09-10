package sptech.classicamoveis.Usuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados de usuário retornados pela API")
public class UsuarioResponseDto {

    @Schema(example = "5")
    private Integer id;
    @Schema(example = "joao.lima")
    private String login;

    public UsuarioResponseDto() {
    }

    public UsuarioResponseDto(Integer id, String login) {
        this.id = id;
        this.login = login;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
}
