package sptech.classicamoveis.Usuario;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UsuarioResponseDto> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public UsuarioResponseDto buscarPorId(Integer id) {
        return toResponseDTO(buscarEntidadePorId(id));
    }

    public UsuarioResponseDto criar(UsuarioRequestDto dto) {
        usuarioRepository.findByLogin(dto.login()).ifPresent(u -> {
            throw new EntityExistsException("Já existe um usuário com o login: " + dto.login());
        });

        Usuario usuario = new Usuario();
        usuario.setLogin(dto.login());
        usuario.setSenha(passwordEncoder.encode(dto.senha())); // nunca salva em texto puro

        return toResponseDTO(usuarioRepository.save(usuario));
    }

    public void deletar(Integer id) {
        usuarioRepository.delete(buscarEntidadePorId(id));
    }

    private Usuario buscarEntidadePorId(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com id: " + id));
    }

    private UsuarioResponseDto toResponseDTO(Usuario u) {
        return new UsuarioResponseDto(u.getId(), u.getLogin());
    }
}