package sptech.classicamoveis.Permissao;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PermissaoServiceTest {
    @Mock PermissaoRepository repository;
    @InjectMocks PermissaoService service;

    @Test void listaTodasAsPermissoes() {
        when(repository.findAll()).thenReturn(List.of(new Permissao(1, "ADMIN"), new Permissao(2, "VENDA")));
        assertEquals(List.of("ADMIN", "VENDA"), service.listarTodos().stream().map(PermissaoResponseDto::getNome).toList());
    }
    @Test void buscaPermissaoPorId() {
        when(repository.findById(1)).thenReturn(Optional.of(new Permissao(1, "ADMIN")));
        assertEquals("ADMIN", service.buscarPorId(1).getNome());
    }
    @Test void falhaAoBuscarIdInexistente() {
        when(repository.findById(99)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.buscarPorId(99));
    }
    @Test void criaPermissaoComNomeDoDto() {
        when(repository.save(any())).thenAnswer(i -> { Permissao p=i.getArgument(0); p.setId(3); return p; });
        assertEquals("ESTOQUE", service.criar(new PermissaoRequestDto("ESTOQUE")).getNome());
    }
    @Test void atualizaPermissaoExistente() {
        Permissao p = new Permissao(1, "ANTIGA"); when(repository.findById(1)).thenReturn(Optional.of(p)); when(repository.save(p)).thenReturn(p);
        assertEquals("NOVA", service.atualizar(1, new PermissaoRequestDto("NOVA")).getNome());
    }
    @Test void excluiPermissaoExistente() {
        Permissao p = new Permissao(1, "ADMIN"); when(repository.findById(1)).thenReturn(Optional.of(p));
        service.deletar(1); verify(repository).delete(p);
    }
}
