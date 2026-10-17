package sptech.classicamoveis.Representante.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sptech.classicamoveis.Fornecedor.model.Fornecedor;
import sptech.classicamoveis.Fornecedor.repository.FornecedorRepository;
import sptech.classicamoveis.Representante.dto.RepresentanteRequestDTO;
import sptech.classicamoveis.Representante.model.Representante;
import sptech.classicamoveis.Representante.repository.RepresentanteRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RepresentanteServiceTest {
    @Mock RepresentanteRepository representanteRepository;
    @Mock FornecedorRepository fornecedorRepository;
    @InjectMocks RepresentanteService service;

    private Representante representante() {
        Representante r = new Representante();
        r.setId(1L); r.setNome("Ana"); r.setTelefone1("11999990000");
        r.setTelefone2("1133334444"); r.setFornecedores(new HashSet<>());
        return r;
    }
    private RepresentanteRequestDTO dto(List<Long> ids) {
        return new RepresentanteRequestDTO("Ana", "11999990000", "1133334444", ids);
    }

    @Test void listarSemTermoBuscaTodos() {
        when(representanteRepository.findAll()).thenReturn(List.of(representante()));
        assertEquals(1, service.listar(null).size());
        verify(representanteRepository).findAll();
    }
    @Test void listarComTermoPesquisa() {
        when(representanteRepository.buscarPorTermo("Ana")).thenReturn(List.of(representante()));
        assertEquals("Ana", service.listar("Ana").get(0).nome());
        verify(representanteRepository).buscarPorTermo("Ana");
    }
    @Test void listarComTermoEmBrancoBuscaTodos() {
        when(representanteRepository.findAll()).thenReturn(List.of());
        assertTrue(service.listar("  ").isEmpty());
    }
    @Test void criarSemFornecedores() {
        when(representanteRepository.save(any(Representante.class))).thenAnswer(i -> { Representante r=i.getArgument(0); r.setId(2L); return r; });
        var resposta = service.criar(dto(null));
        assertEquals(2L, resposta.id()); assertEquals("Ana", resposta.nome()); assertTrue(resposta.fornecedores().isEmpty());
    }
    @Test void criarComFornecedor() {
        Fornecedor f = new Fornecedor(); f.setId(4L); f.setNome("Fornecedor A"); f.setCnpj("123");
        when(fornecedorRepository.findById(4L)).thenReturn(Optional.of(f));
        when(representanteRepository.save(any(Representante.class))).thenAnswer(i -> i.getArgument(0));
        var resposta = service.criar(dto(List.of(4L)));
        assertEquals(1, resposta.fornecedores().size()); assertEquals("Fornecedor A", resposta.fornecedores().get(0).nome());
    }
    @Test void criarComFornecedorInexistenteFalha() {
        when(fornecedorRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.criar(dto(List.of(99L))));
        verify(representanteRepository, never()).save(any());
    }
    @Test void buscarPorIdExistente() {
        when(representanteRepository.findById(1L)).thenReturn(Optional.of(representante()));
        assertEquals("Ana", service.buscarPorId(1L).nome());
    }
    @Test void buscarPorIdInexistente() {
        when(representanteRepository.findById(77L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.buscarPorId(77L));
    }
    @Test void atualizarRepresentante() {
        Representante r = representante();
        when(representanteRepository.findById(1L)).thenReturn(Optional.of(r));
        when(representanteRepository.save(r)).thenReturn(r);
        var resposta = service.atualizar(1L, new RepresentanteRequestDTO("Maria", null, null, List.of()));
        assertEquals("Maria", resposta.nome()); assertNull(resposta.telefone1()); assertTrue(resposta.fornecedores().isEmpty());
    }
    @Test void deletarExistente() {
        Representante r = representante(); when(representanteRepository.findById(1L)).thenReturn(Optional.of(r));
        service.deletar(1L); verify(representanteRepository).delete(r);
    }
    @Test void listarPorFornecedorComRepresentantes() {
        Fornecedor f = new Fornecedor(); f.setId(3L); f.setNome("Fornecedor"); f.setCnpj("123");
        Representante r = representante(); f.setRepresentantes(Set.of(r));
        when(fornecedorRepository.findById(3L)).thenReturn(Optional.of(f));
        var resposta = service.listarPorFornecedor(3L);
        assertEquals("Fornecedor", resposta.nome()); assertEquals(1, resposta.representantes().size());
        assertEquals("Ana", resposta.representantes().get(0).nome());
    }
    @Test void listarPorFornecedorInexistente() {
        when(fornecedorRepository.findById(8L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.listarPorFornecedor(8L));
    }
}
