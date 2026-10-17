package sptech.classicamoveis.Jwt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import sptech.classicamoveis.Colaborador.model.Colaborador;
import sptech.classicamoveis.Jwt.model.UsuarioAutenticado;
import sptech.classicamoveis.Jwt.provider.AutenticacaoProvider;
import sptech.classicamoveis.Jwt.service.AutenticacaoService;
import sptech.classicamoveis.Usuario.model.Usuario;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AutenticacaoProviderTest {

    @Mock AutenticacaoService autenticacaoService;
    @Mock PasswordEncoder passwordEncoder;

    private AutenticacaoProvider provider() {
        return new AutenticacaoProvider(autenticacaoService, passwordEncoder);
    }

    private UsuarioAutenticado usuario(boolean deFerias) {
        return new UsuarioAutenticado(new Usuario(1, "ana", "hash"), new Colaborador(), deFerias);
    }

    private Authentication credenciais() {
        return new UsernamePasswordAuthenticationToken("ana", "senha123");
    }

    @Test
    void autenticaComSenhaCorreta() {
        UsuarioAutenticado u = usuario(false);
        when(autenticacaoService.loadUserByUsername("ana")).thenReturn(u);
        when(passwordEncoder.matches("senha123", "hash")).thenReturn(true);

        Authentication resultado = provider().authenticate(credenciais());

        assertTrue(resultado.isAuthenticated());
        assertSame(u, resultado.getPrincipal());
    }

    @Test
    void rejeitaSenhaIncorreta() {
        when(autenticacaoService.loadUserByUsername("ana")).thenReturn(usuario(false));
        when(passwordEncoder.matches("senha123", "hash")).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> provider().authenticate(credenciais()));
    }

    @Test
    void bloqueiaColaboradorDeFerias() {
        when(autenticacaoService.loadUserByUsername("ana")).thenReturn(usuario(true));
        when(passwordEncoder.matches("senha123", "hash")).thenReturn(true);

        assertThrows(LockedException.class, () -> provider().authenticate(credenciais()));
    }

    @Test
    void suportaApenasUsernamePasswordAuthenticationToken() {
        assertTrue(provider().supports(UsernamePasswordAuthenticationToken.class));
        assertFalse(provider().supports(TestingAuthenticationToken.class));
    }
}
