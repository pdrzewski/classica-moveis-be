package sptech.classicamoveis.Jwt.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import sptech.classicamoveis.Jwt.service.GerenciadorTokenJwt;
import sptech.classicamoveis.Jwt.dto.LoginDto;
import sptech.classicamoveis.Jwt.dto.LoginResponseDto;
import sptech.classicamoveis.Jwt.mapper.LoginMapper;
import sptech.classicamoveis.Jwt.model.UsuarioAutenticado;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/login")
public class LoginController {

    public static final String COOKIE_NOME = "moveis_jwt_token";

    @Value("${app.cookie.secure:false}")
    private boolean cookieSecure;

    private final AuthenticationManager authenticationManager;
    private final GerenciadorTokenJwt jwtTokenManager;
    private final LoginMapper loginMapper;

    public LoginController(AuthenticationManager authenticationManager, GerenciadorTokenJwt jwtTokenManager, LoginMapper loginMapper) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenManager = jwtTokenManager;
        this.loginMapper = loginMapper;
    }

    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginDto dto, HttpServletResponse response) {
        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getLogin(), dto.getSenha()));
        } catch (LockedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenManager.generateToken(authentication);
        response.addHeader(HttpHeaders.SET_COOKIE, montarCookie(token, jwtTokenManager.getJwtTokenValidity()).toString());

        UsuarioAutenticado usuarioAutenticado = (UsuarioAutenticado) authentication.getPrincipal();

        List<String> permissoes = new ArrayList<>();
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            permissoes.add(authority.getAuthority());
        }

        LoginResponseDto responseDto = loginMapper.toResponseDTO(usuarioAutenticado, permissoes);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/sair")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        response.addHeader(HttpHeaders.SET_COOKIE, montarCookie("", 0).toString());
        return ResponseEntity.ok().build();
    }

    private ResponseCookie montarCookie(String token, long maxAgeSegundos) {
        return ResponseCookie.from(COOKIE_NOME, token)
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/")
                .maxAge(maxAgeSegundos)
                .sameSite("Strict")
                .build();
    }
}