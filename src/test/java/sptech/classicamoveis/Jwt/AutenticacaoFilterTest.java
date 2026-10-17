package sptech.classicamoveis.Jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import sptech.classicamoveis.Jwt.controller.LoginController;
import sptech.classicamoveis.Jwt.filter.AutenticacaoFilter;
import sptech.classicamoveis.Jwt.service.AutenticacaoService;
import sptech.classicamoveis.Jwt.service.GerenciadorTokenJwt;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AutenticacaoFilterTest {

    @Mock AutenticacaoService autenticacaoService;
    @Mock GerenciadorTokenJwt tokenManager;

    private AutenticacaoFilter filter;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockFilterChain chain;
    private final UserDetails userDetails = User.withUsername("ana").password("x").authorities("VENDER").build();

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
        filter = new AutenticacaoFilter(autenticacaoService, tokenManager);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        chain = new MockFilterChain();
        when(autenticacaoService.loadUserByUsername("ana")).thenReturn(userDetails);
    }

    @AfterEach
    void limpar() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void semTokenSegueSemAutenticar() throws Exception {
        filter.doFilter(request, response, chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        assertNotNull(chain.getRequest());
        verifyNoInteractions(tokenManager);
    }

    @Test
    void autenticaPeloCookie() throws Exception {
        request.setCookies(new Cookie(LoginController.COOKIE_NOME, "tok"));
        when(tokenManager.getUsernameFromToken("tok")).thenReturn("ana");
        when(tokenManager.validateToken("tok", userDetails)).thenReturn(true);

        filter.doFilter(request, response, chain);

        var auth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(auth);
        assertSame(userDetails, auth.getPrincipal());
        assertNotNull(chain.getRequest());
    }

    @Test
    void autenticaPeloHeaderBearer() throws Exception {
        request.addHeader("Authorization", "Bearer abc");
        when(tokenManager.getUsernameFromToken("abc")).thenReturn("ana");
        when(tokenManager.validateToken("abc", userDetails)).thenReturn(true);

        filter.doFilter(request, response, chain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void ignoraCookieDeOutroNomeEUsaOHeader() throws Exception {
        request.setCookies(new Cookie("outro", "x"));
        request.addHeader("Authorization", "Bearer abc");
        when(tokenManager.getUsernameFromToken("abc")).thenReturn("ana");
        when(tokenManager.validateToken("abc", userDetails)).thenReturn(true);

        filter.doFilter(request, response, chain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void headerSemPrefixBearerEhIgnorado() throws Exception {
        request.addHeader("Authorization", "Basic abc");

        filter.doFilter(request, response, chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verifyNoInteractions(tokenManager);
    }

    @Test
    void tokenNaoValidadoNaoAutentica() throws Exception {
        request.addHeader("Authorization", "Bearer abc");
        when(tokenManager.getUsernameFromToken("abc")).thenReturn("ana");
        when(tokenManager.validateToken("abc", userDetails)).thenReturn(false);

        filter.doFilter(request, response, chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        assertNotNull(chain.getRequest());
    }

    @Test
    void tokenExpiradoNaoQuebraARequisicao() throws Exception {
        request.addHeader("Authorization", "Bearer abc");
        when(tokenManager.getUsernameFromToken("abc")).thenThrow(new ExpiredJwtException(null, null, "expirado"));

        filter.doFilter(request, response, chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        assertNotNull(chain.getRequest());
    }

    @Test
    void tokenMalformadoNaoQuebraARequisicao() throws Exception {
        request.addHeader("Authorization", "Bearer abc");
        when(tokenManager.getUsernameFromToken("abc")).thenThrow(new MalformedJwtException("ruim"));

        filter.doFilter(request, response, chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        assertNotNull(chain.getRequest());
    }

    @Test
    void tokenVazioLancaIllegalArgumentEEhTratado() throws Exception {
        request.addHeader("Authorization", "Bearer ");
        when(tokenManager.getUsernameFromToken("")).thenThrow(new IllegalArgumentException("vazio"));

        filter.doFilter(request, response, chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void contextoJaAutenticadoNaoEhSobrescrito() throws Exception {
        var existente = new UsernamePasswordAuthenticationToken("outro", null, java.util.List.of());
        SecurityContextHolder.getContext().setAuthentication(existente);
        request.addHeader("Authorization", "Bearer abc");
        when(tokenManager.getUsernameFromToken("abc")).thenReturn("ana");

        filter.doFilter(request, response, chain);

        assertSame(existente, SecurityContextHolder.getContext().getAuthentication());
        verify(autenticacaoService, never()).loadUserByUsername(any());
    }
}
