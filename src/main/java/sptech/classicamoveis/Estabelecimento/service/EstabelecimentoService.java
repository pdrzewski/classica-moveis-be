package sptech.classicamoveis.Estabelecimento.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sptech.classicamoveis.Colaborador.model.Colaborador;
import sptech.classicamoveis.Colaborador.repository.ColaboradorRepository;
import sptech.classicamoveis.Endereco.Endereco;
import sptech.classicamoveis.Endereco.repository.EnderecoRepository;
import sptech.classicamoveis.Estabelecimento.Estabelecimento;
import sptech.classicamoveis.Estabelecimento.dto.EstabelecimentoRequestDto;
import sptech.classicamoveis.Estabelecimento.dto.EstabelecimentoComEnderecoRequestDto;
import sptech.classicamoveis.Estabelecimento.dto.EstabelecimentoResponseDto;
import sptech.classicamoveis.Estabelecimento.repository.EstabelecimentoRepository;
import sptech.classicamoveis.Estabelecimento.mapper.EstabelecimentoMapper;

import java.util.List;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Transactional
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

    public EstabelecimentoResponseDto criar(EstabelecimentoComEnderecoRequestDto dto) {
        Endereco endereco = new Endereco();
        endereco.setCep(normalizarCep(dto.getCep()));
        endereco.setLogradouro(dto.getLogradouro());
        endereco.setBairro(dto.getBairro());
        endereco.setCidade(dto.getCidade());
        endereco.setNumero(dto.getNumero());
        endereco.setComplemento(dto.getComplemento());
        endereco.setEstado(dto.getEstado());
        
        Endereco enderecoSalvo = enderecoRepository.save(endereco);

        Colaborador responsavel = colaboradorRepository.findById(dto.getResponsavelId())
                .orElseThrow(() -> new EntityNotFoundException("Responsável não encontrado com id: " + dto.getResponsavelId()));

        Estabelecimento estabelecimento = new Estabelecimento();
        estabelecimento.setNome(dto.getNome());
        estabelecimento.setCnpj(dto.getCnpj());
        estabelecimento.setTelefone(dto.getTelefone());
        estabelecimento.setEndereco(enderecoSalvo);
        estabelecimento.setResponsavel(responsavel);

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

    private String normalizarCep(String cep) {
        if (cep == null) return null;
        String numeros = cep.replaceAll("\\D", "");
        if (numeros.length() != 8) throw new IllegalArgumentException("CEP deve conter 8 números.");
        return numeros;
    }

    private void preencherEntidade(Estabelecimento estabelecimento, EstabelecimentoRequestDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Dados do estabelecimento são obrigatórios.");
        }
        estabelecimento.setNome(dto.getNome());
        estabelecimento.setCnpj(dto.getCnpj());
        estabelecimento.setTelefone(dto.getTelefone());

        Endereco endereco;
        if (dto.getCep() != null || dto.getLogradouro() != null || dto.getBairro() != null
                || dto.getCidade() != null || dto.getNumero() != null || dto.getEstado() != null) {
            endereco = estabelecimento.getEndereco();
            if (dto.getCep() != null) endereco.setCep(normalizarCep(dto.getCep()));
            if (dto.getLogradouro() != null) endereco.setLogradouro(dto.getLogradouro());
            if (dto.getBairro() != null) endereco.setBairro(dto.getBairro());
            if (dto.getCidade() != null) endereco.setCidade(dto.getCidade());
            if (dto.getNumero() != null) endereco.setNumero(dto.getNumero());
            if (dto.getComplemento() != null) endereco.setComplemento(dto.getComplemento());
            if (dto.getEstado() != null) endereco.setEstado(dto.getEstado());
            enderecoRepository.save(endereco);
        } else if (dto.getEnderecoId() != null) {
            endereco = enderecoRepository.findById(dto.getEnderecoId())
                    .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado com id: " + dto.getEnderecoId()));
        } else {
            endereco = estabelecimento.getEndereco();
        }
        estabelecimento.setEndereco(endereco);

        Colaborador responsavel = colaboradorRepository.findById(dto.getResponsavelId())
                .orElseThrow(() -> new EntityNotFoundException("Responsável não encontrado com id: " + dto.getResponsavelId()));
        estabelecimento.setResponsavel(responsavel);
    }
}
