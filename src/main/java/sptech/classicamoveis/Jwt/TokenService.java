package sptech.classicamoveis.Jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sptech.classicamoveis.Usuario.Usuario;

import java.security.Key;
import java.util.Date;

@Service
public class TokenService {

    @Value("${api.security.token.secret:minha-chave-secreta-super-ultra-mega-forte-com-mais-de-32-caracteres}")
    private String secret;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String gerarToken(Usuario usuario) {
        return Jwts.builder()
                .setIssuer("ClassicaMoveis-API") // Na versão 0.11.x usa-se 'setIssuer'
                .setSubject(usuario.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 dia
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Passando a chave e o algoritmo explicitamente
                .compact();
    }

    public String validarToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder() // Na versão 0.11.x usa-se 'parserBuilder()'
                    .setSigningKey(getSigningKey()) // E 'setSigningKey' para validar
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (Exception e) {
            return null; // Retorna null caso o token seja inválido ou esteja expirado
        }
    }
}