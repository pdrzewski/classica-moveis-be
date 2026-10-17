package sptech.classicamoveis.Jwt;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import sptech.classicamoveis.Cargo.model.Cargo;
import sptech.classicamoveis.Colaborador.model.Colaborador;
import sptech.classicamoveis.Jwt.model.UsuarioAutenticado;
import sptech.classicamoveis.Permissao.model.Permissao;
import sptech.classicamoveis.Usuario.model.Usuario;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioAutenticadoTest {

    private final Usuario usuario = new Usuario(1, "ana", "hash");

    @Test
    void expoeLoginESenhaDoUsuario() {
        UsuarioAutenticado u = new UsuarioAutenticado(usuario, new Colaborador(), false);

        assertEquals("ana", u.getUsername());
        assertEquals("hash", u.getPassword());
        assertSame(usuario, u.getUsuario());
    }

    @Test
    void autoridadesVemDasPermissoesDoCargo() {
        Cargo cargo = new Cargo();
        cargo.setPermissoes(Set.of(new Permissao(1, "VENDER")));
        Colaborador colaborador = new Colaborador();
        colaborador.setCargo(cargo);

        var autoridades = new UsuarioAutenticado(usuario, colaborador, false).getAuthorities();

        assertEquals(1, autoridades.size());
        assertEquals("VENDER", autoridades.iterator().next().getAuthority());
    }

    @Test
    void semColaboradorNaoHaAutoridades() {
        assertTrue(new UsuarioAutenticado(usuario, null, false).getAuthorities().isEmpty());
    }

    @Test
    void semCargoNaoHaAutoridades() {
        assertTrue(new UsuarioAutenticado(usuario, new Colaborador(), false).getAuthorities().isEmpty());
    }

    @Test
    void contaBloqueadaSomenteQuandoDeFerias() {
        assertTrue(new UsuarioAutenticado(usuario, null, false).isAccountNonLocked());
        assertFalse(new UsuarioAutenticado(usuario, null, true).isAccountNonLocked());
    }

    @Test
    void demaisFlagsSempreVerdadeiras() {
        UsuarioAutenticado u = new UsuarioAutenticado(usuario, null, false);

        assertTrue(u.isAccountNonExpired());
        assertTrue(u.isCredentialsNonExpired());
        assertTrue(u.isEnabled());
    }
}
