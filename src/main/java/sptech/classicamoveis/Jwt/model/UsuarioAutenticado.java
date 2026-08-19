package sptech.classicamoveis.Jwt.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import sptech.classicamoveis.Colaborador.Colaborador;
import sptech.classicamoveis.Usuario.model.Usuario;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class UsuarioAutenticado implements UserDetails {

    private final Usuario usuario;
    private final Colaborador colaborador;

    public UsuarioAutenticado(Usuario usuario, Colaborador colaborador) {
        this.usuario = usuario;
        this.colaborador = colaborador;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Colaborador getColaborador() {
        return colaborador;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (colaborador == null || colaborador.getCargo() == null) {
            return Collections.emptyList();
        }
        return colaborador.getCargo().getPermissoes().stream()
                .map(permissao -> new SimpleGrantedAuthority(permissao.getPermissao()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return usuario.getSenha();
    }

    @Override
    public String getUsername() {
        return usuario.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
