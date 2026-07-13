package sptech.classicamoveis.Permissao;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissaoService {

    private final PermissaoRepository permissaoRepository;

    public List<PermissaoResponseDto> listarTodos() {
        return permissaoRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public PermissaoResponseDto buscarPorId(Integer id) {
        return toResponseDTO(buscarEntidadePorId(id));
    }

    public PermissaoResponseDto criar(PermissaoRequestDto dto) {
        Permissao p = new Permissao();
        p.setPermissao(dto.getNome());
        return toResponseDTO(permissaoRepository.save(p));
    }

    public PermissaoResponseDto atualizar(Integer id, PermissaoRequestDto dto) {
        Permissao p = buscarEntidadePorId(id);
        p.setPermissao(dto.getNome());
        return toResponseDTO(permissaoRepository.save(p));
    }

    public void deletar(Integer id) {
        permissaoRepository.delete(buscarEntidadePorId(id));
    }

    private Permissao buscarEntidadePorId(Integer id) {
        return permissaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Permissão não encontrada com id: " + id));
    }

    private PermissaoResponseDto toResponseDTO(Permissao p) {
        return new PermissaoResponseDto(p.getId(), p.getPermissao());
    }
}
