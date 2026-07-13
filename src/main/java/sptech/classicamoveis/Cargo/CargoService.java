package sptech.classicamoveis.Cargo;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sptech.classicamoveis.Permissao.Permissao;
import sptech.classicamoveis.Permissao.PermissaoRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CargoService {

    private final CargoRepository cargoRepository;
    private final PermissaoRepository permissaoRepository;

    public List<CargoResponseDto> listarTodos() {
        return cargoRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public CargoResponseDto buscarPorId(Integer id) {
        return toResponseDTO(buscarEntidadePorId(id));
    }

    public CargoResponseDto criar(CargoRequestDto dto) {
        Cargo cargo = new Cargo();
        cargo.setCargo(dto.getNome());
        return toResponseDTO(cargoRepository.save(cargo));
    }

    public CargoResponseDto atualizar(Integer id, CargoRequestDto dto) {
        Cargo cargo = buscarEntidadePorId(id);
        cargo.setCargo(dto.getNome());
        return toResponseDTO(cargoRepository.save(cargo));
    }

    public void deletar(Integer id) {
        cargoRepository.delete(buscarEntidadePorId(id));
    }

    private Cargo buscarEntidadePorId(Integer id) {
        return cargoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cargo não encontrado com id: " + id));
    }

    private Set<Permissao> resolverPermissoes(Set<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return Set.of();
        }
        return ids.stream()
                .map(idPermissao -> permissaoRepository.findById(idPermissao)
                        .orElseThrow(() -> new EntityNotFoundException("Permissão não encontrada com id: " + idPermissao)))
                .collect(Collectors.toSet());
    }

    private CargoResponseDto toResponseDTO(Cargo cargo) {
        Set<String> nomes = cargo.getPermissoes().stream()
                .map(Permissao::getPermissao)
                .collect(Collectors.toSet());
        return new CargoResponseDto(cargo.getId(), cargo.getCargo(), nomes);
    }
}