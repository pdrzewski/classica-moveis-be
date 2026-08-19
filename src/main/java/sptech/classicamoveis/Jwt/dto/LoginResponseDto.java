package sptech.classicamoveis.Jwt.dto;

import java.util.List;

public class LoginResponseDto {

    private String login;
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
