package sptech.classicamoveis.Jwt.dto;

import sptech.classicamoveis.Colaborador.dto.ColaboradorResponseDto;

import java.util.List;

public class LoginResponseDto {

    private String login;
    private List<String> permissoes;
    private Integer usuarioId;
    private ColaboradorResponseDto colaborador;

    public LoginResponseDto() {
    }

    public LoginResponseDto(String login, List<String> permissoes, Integer usuarioId, ColaboradorResponseDto colaborador) {
        this.login = login;
        this.permissoes = permissoes;
        this.usuarioId = usuarioId;
        this.colaborador = colaborador;
    }

    public String getLogin() {
        return login;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
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

    public ColaboradorResponseDto getColaborador() {
        return colaborador;
    }

    public void setColaborador(ColaboradorResponseDto colaborador) {
        this.colaborador = colaborador;
    }
}
