package sptech.classicamoveis.Categoria.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sptech.classicamoveis.Categoria.Categoria;
import sptech.classicamoveis.Categoria.dto.CategoriaRequestDto;
import sptech.classicamoveis.Categoria.repository.CategoriaRepository;
import sptech.classicamoveis.Categoria.dto.CategoriaResponseDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public List<CategoriaResponseDto> listarTodos() {
        return categoriaRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public CategoriaResponseDto buscarPorId(Integer id) {
        return toResponseDTO(buscarEntidadePorId(id));
    }

    public CategoriaResponseDto criar(CategoriaRequestDto dto) {
        Categoria categoria = new Categoria();
        categoria.setCategoria(validarCategoria(dto));
        return toResponseDTO(categoriaRepository.save(categoria));
    }

    public CategoriaResponseDto atualizar(Integer id, CategoriaRequestDto dto) {
        Categoria categoria = buscarEntidadePorId(id);
        categoria.setCategoria(validarCategoria(dto));
        return toResponseDTO(categoriaRepository.save(categoria));
    }

    public void deletar(Integer id) {
        categoriaRepository.delete(buscarEntidadePorId(id));
    }

    private Categoria buscarEntidadePorId(Integer id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada com id: " + id));
    }

    private String validarCategoria(CategoriaRequestDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Dados da categoria são obrigatórios.");
        }

        String categoria = dto.getCategoria();
        if (categoria == null || categoria.isBlank()) {
            categoria = dto.getNome();
        }

        if (categoria == null || categoria.isBlank()) {
            throw new IllegalArgumentException("O nome da categoria é obrigatório.");
        }

        return categoria.trim();
    }

    private CategoriaResponseDto toResponseDTO(Categoria categoria) {
        return new CategoriaResponseDto(categoria.getId(), categoria.getCategoria());
    }
}
