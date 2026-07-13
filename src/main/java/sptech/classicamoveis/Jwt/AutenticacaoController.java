package sptech.classicamoveis.Jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import sptech.classicamoveis.Usuario.Usuario;
import sptech.classicamoveis.Usuario.UsuarioRepository;

@RestController
@RequestMapping("/cadastro")
@RequiredArgsConstructor
public class AutenticacaoController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody LoginRequestDto dto) {
        var authToken = new UsernamePasswordAuthenticationToken(dto.getLogin(), dto.getSenha());

        var authentication = authenticationManager.authenticate(authToken);

        String token = tokenService.gerarToken((Usuario) authentication.getPrincipal());

        return ResponseEntity.ok(new TokenResponseDto(token));
    }

    @PostMapping("/registrar")
    public ResponseEntity<String> registrar(@RequestBody LoginRequestDto dto) {
        if (usuarioRepository.findByLogin(dto.getLogin()).isPresent()) {
            return ResponseEntity.badRequest().body("Esse login já está cadastrado");
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setLogin(dto.getLogin());

        novoUsuario.setSenha(passwordEncoder.encode(dto.getSenha()));

        usuarioRepository.save(novoUsuario);
        return ResponseEntity.ok("Usuário cadastrado com sucesso!");
    }
}