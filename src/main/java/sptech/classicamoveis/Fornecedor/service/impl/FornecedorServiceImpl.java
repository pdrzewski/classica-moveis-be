package sptech.classicamoveis.Fornecedor.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sptech.classicamoveis.Endereco.Endereco;
import sptech.classicamoveis.Endereco.service.EnderecoService;
import sptech.classicamoveis.Fornecedor.dto.FornecedorRequestDTO;
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
    public FornecedorResponseDTO criar(FornecedorRequestDTO dto) {
        Endereco endereco = enderecoService.buscarEntidadePorId(dto.enderecoId());
        Fornecedor fornecedor = fornecedorMapper.toEntity(dto, endereco);
        return fornecedorMapper.toResponseDTO(fornecedorRepository.save(fornecedor));
    }

    @Override
    public FornecedorResponseDTO atualizar(Long id, FornecedorRequestDTO dto) {
        Fornecedor fornecedor = buscarEntidadePorId(id);
        Endereco endereco = enderecoService.buscarEntidadePorId(dto.enderecoId());
        fornecedorMapper.updateEntityFromDto(dto, endereco, fornecedor);
        return fornecedorMapper.toResponseDTO(fornecedorRepository.save(fornecedor));
    }

    @Override
    public void deletar(Long id) {
        Fornecedor fornecedor = buscarEntidadePorId(id);
        fornecedorRepository.delete(fornecedor);
    }

    private Fornecedor buscarEntidadePorId(Long id) {
        return fornecedorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Fornecedor não encontrado com id: " + id));
    }
}
