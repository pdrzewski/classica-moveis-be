package sptech.classicamoveis.Jwt.controller;


import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import sptech.classicamoveis.Jwt.service.GerenciadorTokenJwt;
import sptech.classicamoveis.Jwt.dto.LoginDto;
import sptech.classicamoveis.Jwt.dto.LoginResponseDto;
import sptech.classicamoveis.Jwt.model.UsuarioAutenticado;

import java.util.List;

@RestController
@RequestMapping("/login")
public class LoginController {

    public static final String COOKIE_NOME = "moveis_jwt_token";

    private final AuthenticationManager authenticationManager;
    private final GerenciadorTokenJwt jwtTokenManager;

    public LoginController(AuthenticationManager authenticationManager, GerenciadorTokenJwt jwtTokenManager) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenManager = jwtTokenManager;
    }

    @PostMapping
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto dto, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getLogin(), dto.getSenha()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenManager.generateToken(authentication);
        response.addHeader(HttpHeaders.SET_COOKIE, montarCookie(token, jwtTokenManager.getJwtTokenValidity()).toString());

        UsuarioAutenticado usuarioAutenticado = (UsuarioAutenticado) authentication.getPrincipal();
        List<String> permissoes = authentication.getAuthorities().stream()
                .map(Object::toString)
                .toList();

        return ResponseEntity.ok(new LoginResponseDto(usuarioAutenticado.getUsername(), permissoes));
    }

    @PostMapping("/sair")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        response.addHeader(HttpHeaders.SET_COOKIE, montarCookie("", 0).toString());
        return ResponseEntity.ok().build();
    }

    private ResponseCookie montarCookie(String token, long maxAgeSegundos) {
        return ResponseCookie.from(COOKIE_NOME, token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(maxAgeSegundos)
                .sameSite("Lax")
                .build();
    }
}
