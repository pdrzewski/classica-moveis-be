package sptech.classicamoveis.Colaborador;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sptech.classicamoveis.Cargo.Cargo;
import sptech.classicamoveis.Cargo.CargoRepository;
import sptech.classicamoveis.Usuario.Usuario;
import sptech.classicamoveis.Usuario.UsuarioRepository;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
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
        Colaborador colaborador = buscarEntidadePorId(id);
        Integer usuarioId = colaborador.getUsuario() != null ? colaborador.getUsuario().getId() : null;

        colaboradorRepository.delete(colaborador);

        if (usuarioId != null) {
            usuarioRepository.deleteById(usuarioId);
        }
    }


    public ColaboradorResponseDto registrarFerias(Integer id, FeriasRequestDto dto) {
        if (dto.dataInicio() == null || dto.dataFim() == null) {
            throw new IllegalArgumentException("dataInicio e dataFim são obrigatórias.");
        }
        if (dto.dataFim().isBefore(dto.dataInicio())) {
            throw new IllegalArgumentException("dataFim não pode ser anterior à dataInicio.");
        }

        Colaborador colaborador = buscarEntidadePorId(id);
        colaborador.setFeriasDataInicio(dto.dataInicio());
        colaborador.setFeriasDataFim(dto.dataFim());
        colaborador.setEmFerias(true);

        return toResponseDTO(colaboradorRepository.save(colaborador));
    }

    public ColaboradorResponseDto encerrarFerias(Integer id) {
        Colaborador colaborador = buscarEntidadePorId(id);
        colaborador.setEmFerias(false);
        return toResponseDTO(colaboradorRepository.save(colaborador));
    }


    public List<AniversarioColaboradorDto> buscarAniversariosProximos(int dias) {
        LocalDate hoje = LocalDate.now();

        return colaboradorRepository.findAll().stream()
                .filter(c -> c.getDataNascimento() != null)
                .map(c -> new AniversarioColaboradorDto(
                        c.getId(),
                        c.getNome(),
                        c.getDataNascimento(),
                        diasParaProximoAniversario(c.getDataNascimento(), hoje)
                ))
                .filter(dto -> dto.diasParaAniversario() <= dias)
                .sorted(Comparator.comparingInt(AniversarioColaboradorDto::diasParaAniversario))
                .collect(Collectors.toList());
    }

    private int diasParaProximoAniversario(LocalDate nascimento, LocalDate hoje) {
        LocalDate proximo = ajustarParaAno(nascimento, hoje.getYear());
        if (proximo.isBefore(hoje)) {
            proximo = ajustarParaAno(nascimento, hoje.getYear() + 1);
        }
        return (int) ChronoUnit.DAYS.between(hoje, proximo);
    }

    private LocalDate ajustarParaAno(LocalDate data, int ano) {
        try {
            return data.withYear(ano);
        } catch (DateTimeException e) {
            // Caso de aniversário em 29/fev num ano não bissexto
            return LocalDate.of(ano, 2, 28);
        }
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
                c.getFeriasDataInicio(),
                c.getFeriasDataFim(),
                c.getDataAdmissao(),
                c.getDataNascimento(),
                c.getSalario(),
                c.getCarteiraTrabalho(),
                c.getComissao()
        );
    }
}