package sptech.classicamoveis.Fornecedor.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sptech.classicamoveis.Endereco.Endereco;
import sptech.classicamoveis.Endereco.repository.EnderecoRepository;
import sptech.classicamoveis.Endereco.service.EnderecoService;
import sptech.classicamoveis.Fornecedor.dto.FornecedorRequestDTO;
import sptech.classicamoveis.Fornecedor.dto.FornecedorComEnderecoRequestDTO;
import sptech.classicamoveis.Fornecedor.dto.FornecedorResponseDTO;
import sptech.classicamoveis.Fornecedor.mapper.FornecedorMapper;
import sptech.classicamoveis.Fornecedor.model.Fornecedor;
import sptech.classicamoveis.Fornecedor.repository.FornecedorRepository;
import sptech.classicamoveis.Fornecedor.service.FornecedorService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FornecedorServiceImpl implements FornecedorService {

    private final FornecedorRepository fornecedorRepository;
    private final FornecedorMapper fornecedorMapper;
    private final EnderecoService enderecoService;
    private final EnderecoRepository enderecoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<FornecedorResponseDTO> listarTodos() {
        return fornecedorRepository.findAll()
                .stream()
                .map(fornecedorMapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public FornecedorResponseDTO buscarPorId(Long id) {
        return fornecedorMapper.toResponseDTO(buscarEntidadePorId(id));
    }

    @Override
    public FornecedorResponseDTO criar(FornecedorComEnderecoRequestDTO dto) {
        Endereco endereco = new Endereco();
        endereco.setCep(normalizarCep(dto.cep()));
        endereco.setLogradouro(dto.logradouro());
        endereco.setBairro(dto.bairro());
        endereco.setCidade(dto.cidade());
        endereco.setNumero(dto.numero());
        endereco.setComplemento(dto.complemento());
        endereco.setEstado(dto.estado());

        Endereco enderecoSalvo = enderecoRepository.save(endereco);
        Fornecedor fornecedor = fornecedorMapper.toEntityFromComEnderecoDTO(dto, enderecoSalvo);
        return fornecedorMapper.toResponseDTO(fornecedorRepository.save(fornecedor));
    }

    @Override
    public FornecedorResponseDTO atualizar(Long id, FornecedorRequestDTO dto) {
        Fornecedor fornecedor = buscarEntidadePorId(id);
        Endereco endereco;
        if (dto.cep() != null || dto.logradouro() != null || dto.bairro() != null
                || dto.cidade() != null || dto.numero() != null || dto.estado() != null) {
            endereco = fornecedor.getEndereco();
            if (dto.cep() != null) endereco.setCep(normalizarCep(dto.cep()));
            if (dto.logradouro() != null) endereco.setLogradouro(dto.logradouro());
            if (dto.bairro() != null) endereco.setBairro(dto.bairro());
            if (dto.cidade() != null) endereco.setCidade(dto.cidade());
            if (dto.numero() != null) endereco.setNumero(dto.numero());
            if (dto.complemento() != null) endereco.setComplemento(dto.complemento());
            if (dto.estado() != null) endereco.setEstado(dto.estado());
            enderecoRepository.save(endereco);
        } else if (dto.enderecoId() != null) {
            endereco = enderecoService.buscarEntidadePorId(dto.enderecoId());
        } else {
            endereco = fornecedor.getEndereco();
        }
        fornecedorMapper.updateEntityFromDto(dto, endereco, fornecedor);
        return fornecedorMapper.toResponseDTO(fornecedorRepository.save(fornecedor));
    }

    @Override
    public void deletar(Long id) {
        Fornecedor fornecedor = buscarEntidadePorId(id);
        fornecedorRepository.delete(fornecedor);
    }

    private String normalizarCep(String cep) {
        if (cep == null) return null;
        String numeros = cep.replaceAll("\\D", "");
        if (numeros.length() != 8) throw new IllegalArgumentException("CEP deve conter 8 números.");
        return numeros;
    }

    private Fornecedor buscarEntidadePorId(Long id) {
        return fornecedorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Fornecedor não encontrado com id: " + id));
    }
}
