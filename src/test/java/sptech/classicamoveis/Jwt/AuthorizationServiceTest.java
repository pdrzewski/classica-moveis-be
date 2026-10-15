package sptech.classicamoveis.Jwt;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import sptech.classicamoveis.Jwt.service.AuthorizationService;
import sptech.classicamoveis.Usuario.model.Usuario;
import sptech.classicamoveis.Usuario.repository.UsuarioRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorizationServiceTest {
    @Mock private UsuarioRepository usuarioRepository;
    @InjectMocks private AuthorizationService authorizationService;

    @BeforeEach
    void limparContexto() {
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void restaurarContexto() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void deveRetornarFalseQuandoNaoExisteAutenticacao() {
        assertFalse(authorizationService.temRole("ADMIN"));
        assertFalse(authorizationService.isAdmin());
        assertFalse(authorizationService.podeAcessarUsuario(1));
        assertNull(authorizationService.getIdUsuarioAutenticado());
    }

    @Test
    void deveReconhecerRoleAdmin() {
        autenticar("admin", "ROLE_ADMIN");

        assertTrue(authorizationService.temRole("ADMIN"));
        assertTrue(authorizationService.isAdmin());
        assertTrue(authorizationService.podeAcessarUsuario(999));
        verifyNoInteractions(usuarioRepository);
    }

    @Test
    void deveReconhecerRoleDiferenteDeAdmin() {
        autenticar("vendedor", "ROLE_VENDEDOR");

        assertTrue(authorizationService.temRole("VENDEDOR"));
        assertFalse(authorizationService.temRole("ADMIN"));
        assertFalse(authorizationService.isAdmin());
    }

    @Test
    void devePermitirUsuarioAcessarPropriosDados() {
        autenticar("renan", "ROLE_VENDEDOR");
        when(usuarioRepository.findByLogin("renan"))
                .thenReturn(Optional.of(new Usuario(7, "renan", "senha")));

        assertTrue(authorizationService.podeAcessarUsuario(7));
        assertEquals(7, authorizationService.getIdUsuarioAutenticado());
    }

    @Test
    void deveNegarAcessoAOutroUsuario() {
        autenticar("renan", "ROLE_VENDEDOR");
        when(usuarioRepository.findByLogin("renan"))
                .thenReturn(Optional.of(new Usuario(7, "renan", "senha")));

        assertFalse(authorizationService.podeAcessarUsuario(8));
    }

    @Test
    void deveNegarAcessoQuandoLoginNaoExisteNoBanco() {
        autenticar("desconhecido", "ROLE_VENDEDOR");
        when(usuarioRepository.findByLogin("desconhecido")).thenReturn(Optional.empty());

        assertFalse(authorizationService.podeAcessarUsuario(7));
        assertNull(authorizationService.getIdUsuarioAutenticado());
    }

    private void autenticar(String login, String role) {
        var autenticacao = new UsernamePasswordAuthenticationToken(
                login, "senha", List.of(new SimpleGrantedAuthority(role)));
        SecurityContextHolder.getContext().setAuthentication(autenticacao);
    }
}
