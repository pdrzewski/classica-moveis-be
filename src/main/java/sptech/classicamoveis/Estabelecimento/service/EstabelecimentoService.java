package sptech.classicamoveis.Estabelecimento.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sptech.classicamoveis.Colaborador.model.Colaborador;
import sptech.classicamoveis.Colaborador.repository.ColaboradorRepository;
import sptech.classicamoveis.Endereco.Endereco;
import sptech.classicamoveis.Endereco.repository.EnderecoRepository;
import sptech.classicamoveis.Estabelecimento.Estabelecimento;
import sptech.classicamoveis.Estabelecimento.dto.EstabelecimentoRequestDto;
import sptech.classicamoveis.Estabelecimento.dto.EstabelecimentoResponseDto;
import sptech.classicamoveis.Estabelecimento.repository.EstabelecimentoRepository;
import sptech.classicamoveis.Estabelecimento.mapper.EstabelecimentoMapper;

import java.util.List;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class EstabelecimentoService {

    private final EstabelecimentoRepository estabelecimentoRepository;
    private final EnderecoRepository enderecoRepository;
    private final ColaboradorRepository colaboradorRepository;
    private final EstabelecimentoMapper estabelecimentoMapper;

    public List<EstabelecimentoResponseDto> listarTodos() {
        List<Estabelecimento> entidades = estabelecimentoRepository.findAll();
        List<EstabelecimentoResponseDto> resultados = new ArrayList<>();
        for (Estabelecimento e : entidades) {
            resultados.add(estabelecimentoMapper.toResponseDTO(e));
        }
        return resultados;
    }

    public EstabelecimentoResponseDto buscarPorId(Integer id) {
        return estabelecimentoMapper.toResponseDTO(buscarEntidadePorId(id));
    }

    public EstabelecimentoResponseDto criar(EstabelecimentoRequestDto dto) {
        Estabelecimento estabelecimento = new Estabelecimento();
        preencherEntidade(estabelecimento, dto);
        return estabelecimentoMapper.toResponseDTO(estabelecimentoRepository.save(estabelecimento));
    }

    public EstabelecimentoResponseDto atualizar(Integer id, EstabelecimentoRequestDto dto) {
        Estabelecimento estabelecimento = buscarEntidadePorId(id);
        preencherEntidade(estabelecimento, dto);
        return estabelecimentoMapper.toResponseDTO(estabelecimentoRepository.save(estabelecimento));
    }

    public void deletar(Integer id) {
        estabelecimentoRepository.delete(buscarEntidadePorId(id));
    }

    private Estabelecimento buscarEntidadePorId(Integer id) {
        return estabelecimentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Estabelecimento não encontrado com id: " + id));
    }

    private void preencherEntidade(Estabelecimento estabelecimento, EstabelecimentoRequestDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Dados do estabelecimento são obrigatórios.");
        }
        estabelecimento.setNome(dto.getNome());
        estabelecimento.setCnpj(dto.getCnpj());
        estabelecimento.setTelefone(dto.getTelefone());

        Endereco endereco = enderecoRepository.findById(dto.getEnderecoId())
                .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado com id: " + dto.getEnderecoId()));
        estabelecimento.setEndereco(endereco);

        Colaborador responsavel = colaboradorRepository.findById(dto.getResponsavelId())
                .orElseThrow(() -> new EntityNotFoundException("Responsável não encontrado com id: " + dto.getResponsavelId()));
        estabelecimento.setResponsavel(responsavel);
    }
}
