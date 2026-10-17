package sptech.classicamoveis.Jwt;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;
import sptech.classicamoveis.Cargo.model.Cargo;
import sptech.classicamoveis.Colaborador.model.Colaborador;
import sptech.classicamoveis.Colaborador.service.ColaboradorService;
import sptech.classicamoveis.Jwt.controller.LoginController;
import sptech.classicamoveis.Jwt.dto.LoginDto;
import sptech.classicamoveis.Jwt.dto.LoginResponseDto;
import sptech.classicamoveis.Jwt.mapper.LoginMapper;
import sptech.classicamoveis.Jwt.model.UsuarioAutenticado;
import sptech.classicamoveis.Jwt.service.GerenciadorTokenJwt;
import sptech.classicamoveis.Permissao.model.Permissao;
import sptech.classicamoveis.Usuario.model.Usuario;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    @Mock AuthenticationManager authenticationManager;
    @Mock GerenciadorTokenJwt tokenManager;
    @Mock LoginMapper loginMapper;
    @Mock ColaboradorService colaboradorService;
    @InjectMocks LoginController controller;

    @AfterEach
    void limpar() {
        SecurityContextHolder.clearContext();
    }

    private UsuarioAutenticado usuarioAutenticado() {
        Cargo cargo = new Cargo();
        cargo.setPermissoes(Set.of(new Permissao(1, "VENDER")));
        Colaborador colaborador = new Colaborador();
        colaborador.setCargo(cargo);
        return new UsuarioAutenticado(new Usuario(1, "ana", "hash"), colaborador, false);
    }

    @Test
    void loginComSucessoGeraCookieEResposta() {
        UsuarioAutenticado principal = usuarioAutenticado();
        var auth = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
        LoginResponseDto dto = new LoginResponseDto();
        when(authenticationManager.authenticate(any())).thenReturn(auth);
        when(tokenManager.generateToken(auth)).thenReturn("tok123");
        when(tokenManager.getJwtTokenValidity()).thenReturn(3600L);
        when(loginMapper.toResponseDTO(principal, List.of("VENDER"))).thenReturn(dto);
        MockHttpServletResponse response = new MockHttpServletResponse();

        ResponseEntity<?> r = controller.login(new LoginDto("ana", "senha"), response);

        assertEquals(HttpStatus.OK, r.getStatusCode());
        assertSame(dto, r.getBody());
        String cookie = response.getHeader(HttpHeaders.SET_COOKIE);
        assertTrue(cookie.contains(LoginController.COOKIE_NOME + "=tok123"));
        assertTrue(cookie.contains("HttpOnly"));
        assertTrue(cookie.contains("Max-Age=3600"));
        assertTrue(cookie.contains("SameSite=Strict"));
        assertFalse(cookie.contains("Secure"));
        verify(colaboradorService).verificarFeriasExpiradas(principal.getColaborador());
        assertSame(auth, SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void cookieSecureQuandoConfigurado() {
        ReflectionTestUtils.setField(controller, "cookieSecure", true);
        UsuarioAutenticado principal = usuarioAutenticado();
        var auth = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
        when(authenticationManager.authenticate(any())).thenReturn(auth);
        when(tokenManager.generateToken(auth)).thenReturn("tok");
        when(tokenManager.getJwtTokenValidity()).thenReturn(60L);
        MockHttpServletResponse response = new MockHttpServletResponse();

        controller.login(new LoginDto("ana", "senha"), response);

        assertTrue(response.getHeader(HttpHeaders.SET_COOKIE).contains("Secure"));
    }

    @Test
    void colaboradorDeFeriasRecebe403() {
        when(authenticationManager.authenticate(any())).thenThrow(new LockedException("de férias"));

        ResponseEntity<?> r = controller.login(new LoginDto("ana", "senha"), new MockHttpServletResponse());

        assertEquals(HttpStatus.FORBIDDEN, r.getStatusCode());
        assertEquals("de férias", r.getBody());
        verifyNoInteractions(tokenManager);
    }

    @Test
    void credenciaisInvalidasRecebem401() {
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("inválidas"));

        ResponseEntity<?> r = controller.login(new LoginDto("ana", "errada"), new MockHttpServletResponse());

        assertEquals(HttpStatus.UNAUTHORIZED, r.getStatusCode());
        assertEquals("inválidas", r.getBody());
    }

    @Test
    void logoutExpiraOCookie() {
        MockHttpServletResponse response = new MockHttpServletResponse();

        ResponseEntity<Void> r = controller.logout(response);

        assertEquals(HttpStatus.OK, r.getStatusCode());
        String cookie = response.getHeader(HttpHeaders.SET_COOKIE);
        assertTrue(cookie.contains(LoginController.COOKIE_NOME + "="));
        assertTrue(cookie.contains("Max-Age=0"));
    }
}
