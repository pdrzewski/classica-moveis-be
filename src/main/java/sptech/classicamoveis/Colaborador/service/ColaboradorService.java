package sptech.classicamoveis.Colaborador.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sptech.classicamoveis.Cargo.model.Cargo;
import sptech.classicamoveis.Cargo.repository.CargoRepository;
import sptech.classicamoveis.Colaborador.AniversarioColaboradorDto;
import sptech.classicamoveis.Colaborador.FeriasRequestDto;
import sptech.classicamoveis.Colaborador.dto.ColaboradorRequestDto;
import sptech.classicamoveis.Colaborador.dto.ColaboradorResponseDto;
import sptech.classicamoveis.Colaborador.model.Colaborador;
import sptech.classicamoveis.Colaborador.repository.ColaboradorRepository;
import sptech.classicamoveis.Estabelecimento.Estabelecimento;
import sptech.classicamoveis.Estabelecimento.repository.EstabelecimentoRepository;
import sptech.classicamoveis.Usuario.model.Usuario;
import sptech.classicamoveis.Usuario.repository.UsuarioRepository;

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
    private final EstabelecimentoRepository estabelecimentoRepository;

    public List<ColaboradorResponseDto> listarTodos() {
        return colaboradorRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public ColaboradorResponseDto buscarPorId(Integer id) {
        return toResponseDTO(buscarEntidadePorId(id));
    }

    public ColaboradorResponseDto criar(ColaboradorRequestDto dto) {
        validarDuplicidade(dto);

        Colaborador colaborador = new Colaborador();
        preencherEntidade(colaborador, dto);
        return toResponseDTO(colaboradorRepository.save(colaborador));
    }

    public ColaboradorResponseDto atualizar(Integer id, ColaboradorRequestDto dto) {
        Colaborador colaborador = buscarEntidadePorId(id);
        validarDuplicidade(dto, id);
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
        Estabelecimento estabelecimento = dto.estabelecimentoId() == null ? null :
                estabelecimentoRepository.findById(dto.estabelecimentoId())
                        .orElseThrow(() -> new EntityNotFoundException("Estabelecimento não encontrado com id: " + dto.estabelecimentoId()));

        colaborador.setNome(dto.nome());
        colaborador.setCargo(cargo);
        colaborador.setUsuario(usuario);
        colaborador.setEstabelecimento(estabelecimento);
        colaborador.setEmFerias(dto.emFerias() != null ? dto.emFerias() : false);
        colaborador.setDataAdmissao(dto.dataAdmissao());
        colaborador.setDataNascimento(dto.dataNascimento());
        colaborador.setSalario(dto.salario());
        colaborador.setCarteiraTrabalho(dto.carteiraTrabalho());
        colaborador.setComissao(dto.comissao());
        colaborador.setCpf(dto.cpf());
    }

    private void validarDuplicidade(ColaboradorRequestDto dto) {
        validarDuplicidade(dto, null);
    }

    private void validarDuplicidade(ColaboradorRequestDto dto, Integer colaboradorIdIgnorado) {
        if (dto.usuarioId() != null && colaboradorRepository.existsByUsuario_Id(dto.usuarioId())) {
            Colaborador colaboradorExistente = colaboradorRepository.findByUsuario_Id(dto.usuarioId()).orElse(null);
            if (colaboradorExistente != null && !colaboradorExistente.getId().equals(colaboradorIdIgnorado)) {
                throw new IllegalArgumentException("Já existe um colaborador cadastrado para este usuário.");
            }
        }

        if (dto.cpf() != null && !dto.cpf().isBlank() && colaboradorRepository.existsByCpf(dto.cpf())) {
            Colaborador colaboradorExistente = colaboradorRepository.findAll().stream()
                    .filter(c -> dto.cpf().equals(c.getCpf()))
                    .findFirst()
                    .orElse(null);
            if (colaboradorExistente != null && !colaboradorExistente.getId().equals(colaboradorIdIgnorado)) {
                throw new IllegalArgumentException("Já existe um colaborador cadastrado com este CPF.");
            }
        }
    }

    private Colaborador buscarEntidadePorId(Integer id) {
        return colaboradorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Colaborador não encontrado com id: " + id));
    }

    public boolean estaDeFerias(Colaborador colaborador) {
        if (colaborador == null) {
            return false;
        }

        Boolean emFerias = colaborador.getEmFerias();
        if (emFerias == null || !emFerias) {
            return false;
        }

        LocalDate inicio = colaborador.getFeriasDataInicio();
        LocalDate fim = colaborador.getFeriasDataFim();
        if (inicio != null && fim != null) {
            LocalDate hoje = LocalDate.now();
            return !hoje.isBefore(inicio) && !hoje.isAfter(fim);
        }

        return true;
    }

    public boolean estaDeFerias(Integer colaboradorId) {
        return colaboradorRepository.findById(colaboradorId)
                .map(this::estaDeFerias)
                .orElse(false);
    }

    private ColaboradorResponseDto toResponseDTO(Colaborador c) {
        Integer estabelecimentoId = c.getEstabelecimento() == null ? null : c.getEstabelecimento().getId();

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
                c.getComissao(),
                estabelecimentoId,
                c.getCpf()
        );
    }

    public List<ColaboradorResponseDto> listarEmFerias() {
        return colaboradorRepository.findAll()
                .stream()
                .filter(this::estaDeFerias)
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public void verificarFeriasExpiradas(Colaborador colaborador) {

        if (colaborador.getFeriasDataFim() == null) {
            return;
        }

        LocalDate hoje = LocalDate.now();

        if (hoje.isAfter(colaborador.getFeriasDataFim())) {

            colaborador.setEmFerias(false);
            colaborador.setFeriasDataInicio(null);
            colaborador.setFeriasDataFim(null);

            colaboradorRepository.save(colaborador);
        }
    }
}