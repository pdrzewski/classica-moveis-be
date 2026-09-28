package sptech.classicamoveis.Representante.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sptech.classicamoveis.Fornecedor.model.Fornecedor;
import sptech.classicamoveis.Fornecedor.repository.FornecedorRepository;
import sptech.classicamoveis.Representante.dto.RepresentanteRequestDTO;
import sptech.classicamoveis.Representante.dto.RepresentanteResponseDTO;
import sptech.classicamoveis.Representante.model.Representante;
import sptech.classicamoveis.Representante.repository.RepresentanteRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RepresentanteService {

    private final RepresentanteRepository representanteRepository;
    private final FornecedorRepository fornecedorRepository;

    @Transactional(readOnly = true)
    public List<RepresentanteResponseDTO> listar(String termo) {
        List<Representante> representantes;

        if (termo == null || termo.isBlank()) {
            representantes = representanteRepository.findAll();
        } else {
            representantes = representanteRepository.buscarPorTermo(termo);
        }

        return representantes.stream().map(this::toResponseDTO).toList();
    }

    @Transactional(readOnly = true)
    public RepresentanteResponseDTO buscarPorId(Long id) {
        return toResponseDTO(buscarEntidadePorId(id));
    }

    public RepresentanteResponseDTO criar(RepresentanteRequestDTO dto) {
        Representante representante = new Representante();
        preencher(representante, dto);
        return toResponseDTO(representanteRepository.save(representante));
    }

    public RepresentanteResponseDTO atualizar(Long id, RepresentanteRequestDTO dto) {
        Representante representante = buscarEntidadePorId(id);
        preencher(representante, dto);
        return toResponseDTO(representanteRepository.save(representante));
    }

    public void deletar(Long id) {
        representanteRepository.delete(buscarEntidadePorId(id));
    }

    private void preencher(Representante representante, RepresentanteRequestDTO dto) {
        representante.setNome(dto.nome());
        representante.setTelefone1(dto.telefone1());
        representante.setTelefone2(dto.telefone2());

        HashSet<Fornecedor> fornecedores = new HashSet<>();
        if (dto.fornecedorIds() != null) {
            for (Long fornecedorId : dto.fornecedorIds()) {
                fornecedores.add(fornecedorRepository.findById(fornecedorId)
                        .orElseThrow(() -> new EntityNotFoundException(
                                "Fornecedor não encontrado com id: " + fornecedorId)));
            }
        }
        representante.setFornecedores(fornecedores);
    }

    private Representante buscarEntidadePorId(Long id) {
        return representanteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Representante não encontrado com id: " + id));
    }

    private RepresentanteResponseDTO toResponseDTO(Representante representante) {
        List<RepresentanteResponseDTO.FornecedorRepresentadoDTO> fornecedores = new ArrayList<>();

        for (Fornecedor fornecedor : representante.getFornecedores()) {
            fornecedores.add(new RepresentanteResponseDTO.FornecedorRepresentadoDTO(
                    fornecedor.getId(),
                    fornecedor.getNome(),
                    fornecedor.getCnpj()
            ));
        }

        return new RepresentanteResponseDTO(
                representante.getId(),
                representante.getNome(),
                representante.getTelefone1(),
                representante.getTelefone2(),
                fornecedores
        );
    }
}
