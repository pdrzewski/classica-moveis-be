package sptech.classicamoveis.Jwt.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Resposta do login com o usuário autenticado e suas permissões")
public class LoginResponseDto {

    @Schema(example = "joao.lima")
    private String login;
    @Schema(example = "[\"CRIAR_VENDA\", \"CONSULTAR_ESTOQUE\"]")
    private List<String> permissoes;

    public LoginResponseDto() {
    }

    public LoginResponseDto(String login, List<String> permissoes) {
        this.login = login;
        this.permissoes = permissoes;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public List<String> getPermissoes() {
        return permissoes;
    }

    public void setPermissoes(List<String> permissoes) {
        this.permissoes = permissoes;
    }
}
