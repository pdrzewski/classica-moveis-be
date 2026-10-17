package sptech.classicamoveis.Jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import sptech.classicamoveis.Jwt.service.GerenciadorTokenJwt;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GerenciadorTokenJwtTest {

    private static final String SECRET = "uma-chave-secreta-de-teste-com-mais-de-32-bytes!!";

    private final GerenciadorTokenJwt manager = new GerenciadorTokenJwt(SECRET, 3600);

    private UsernamePasswordAuthenticationToken autenticacao(String login) {
        return new UsernamePasswordAuthenticationToken(login, null,
                List.of(new SimpleGrantedAuthority("VENDER"), new SimpleGrantedAuthority("ESTOQUE")));
    }

    private UserDetails userDetails(String login) {
        return User.withUsername(login).password("x").authorities("VENDER").build();
    }

    @Test
    void geraTokenComUsernameEAuthorities() {
        String token = manager.generateToken(autenticacao("ana"));

        assertEquals("ana", manager.getUsernameFromToken(token));
        assertEquals("VENDER,ESTOQUE",
                manager.getClaimForToken(token, claims -> claims.get("authorities", String.class)));
    }

    @Test
    void expiracaoRespeitaAValidadeConfigurada() {
        String token = manager.generateToken(autenticacao("ana"));

        Date expiracao = manager.getExpirationDateFromToken(token);

        assertTrue(expiracao.after(new Date()));
        assertTrue(expiracao.before(new Date(System.currentTimeMillis() + 3_700_000L)));
        assertEquals(3600L, manager.getJwtTokenValidity());
    }

    @Test
    void tokenValidoParaOMesmoUsuario() {
        String token = manager.generateToken(autenticacao("ana"));
        assertTrue(manager.validateToken(token, userDetails("ana")));
    }

    @Test
    void tokenInvalidoParaOutroUsuario() {
        String token = manager.generateToken(autenticacao("ana"));
        assertFalse(manager.validateToken(token, userDetails("bob")));
    }

    @Test
    void tokenExpiradoLancaExcecao() {
        GerenciadorTokenJwt expirado = new GerenciadorTokenJwt(SECRET, -10);
        String token = expirado.generateToken(autenticacao("ana"));

        assertThrows(ExpiredJwtException.class, () -> manager.getUsernameFromToken(token));
        assertThrows(ExpiredJwtException.class, () -> manager.validateToken(token, userDetails("ana")));
    }

    @Test
    void tokenAssinadoComOutraChaveEhRejeitado() {
        GerenciadorTokenJwt outro = new GerenciadorTokenJwt("outra-chave-secreta-bem-diferente-da-primeira-123", 3600);
        String token = outro.generateToken(autenticacao("ana"));

        assertThrows(SignatureException.class, () -> manager.getUsernameFromToken(token));
    }
}
