package sptech.classicamoveis.Colaborador;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sptech.classicamoveis.Cargo.Cargo; // Certifique-se de que os caminhos dos pacotes estão certos
import sptech.classicamoveis.Cargo.CargoRepository;
import sptech.classicamoveis.Usuario.Usuario;
import sptech.classicamoveis.Usuario.UsuarioRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ColaboradorService {

    private final ColaboradorRepository colaboradorRepository;
    private final CargoRepository cargoRepository;
    private final UsuarioRepository usuarioRepository;

    public List<ColaboradorResponseDto> listarTodos() {
        return colaboradorRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public ColaboradorResponseDto buscarPorId(Integer id) {
        return toResponseDTO(buscarEntidadePorId(id));
    }

    public ColaboradorResponseDto criar(ColaboradorRequestDto dto) {
        Colaborador colaborador = new Colaborador();
        preencherEntidade(colaborador, dto);
        return toResponseDTO(colaboradorRepository.save(colaborador));
    }

    public ColaboradorResponseDto atualizar(Integer id, ColaboradorRequestDto dto) {
        Colaborador colaborador = buscarEntidadePorId(id);
        preencherEntidade(colaborador, dto);
        return toResponseDTO(colaboradorRepository.save(colaborador));
    }

    public void deletar(Integer id) {
        colaboradorRepository.delete(buscarEntidadePorId(id));
    }

    private void preencherEntidade(Colaborador colaborador, ColaboradorRequestDto dto) {
        Cargo cargo = cargoRepository.findById(dto.cargoId())
                .orElseThrow(() -> new EntityNotFoundException("Cargo não encontrado com id: " + dto.cargoId()));
        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com id: " + dto.usuarioId()));

        colaborador.setNome(dto.nome());
        colaborador.setCargo(cargo);
        colaborador.setUsuario(usuario);
        colaborador.setEmFerias(dto.emFerias() != null ? dto.emFerias() : false);
        colaborador.setDataAdmissao(dto.dataAdmissao());
        colaborador.setDataNascimento(dto.dataNascimento());
        colaborador.setSalario(dto.salario());
        colaborador.setCarteiraTrabalho(dto.carteiraTrabalho());
        colaborador.setComissao(dto.comissao());
    }

    private Colaborador buscarEntidadePorId(Integer id) {
        return colaboradorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Colaborador não encontrado com id: " + id));
    }

    private ColaboradorResponseDto toResponseDTO(Colaborador c) {
        return new ColaboradorResponseDto(
                c.getId(),
                c.getNome(),
                c.getCargo() != null ? c.getCargo().getId() : null,
                c.getCargo() != null ? c.getCargo().getCargo() : null,
                c.getUsuario() != null ? c.getUsuario().getId() : null,
                c.getEmFerias(),
                c.getDataAdmissao(),
                c.getDataNascimento(),
                c.getSalario(),
                c.getCarteiraTrabalho(),
                c.getComissao()
        );
    }
}