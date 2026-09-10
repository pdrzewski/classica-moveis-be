package sptech.classicamoveis.Jwt.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Credenciais de acesso")
public class LoginDto {

    @Schema(example = "joao.lima")
    private String login;
    @Schema(example = "SenhaForte@123")
    private String senha;

    public LoginDto() {
    }

    public LoginDto(String login, String senha) {
        this.login = login;
        this.senha = senha;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
