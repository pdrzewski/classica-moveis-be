package sptech.classicamoveis.Permissao.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sptech.classicamoveis.Permissao.dto.PermissaoRequestDto;
import sptech.classicamoveis.Permissao.dto.PermissaoResponseDto;
import sptech.classicamoveis.Permissao.service.PermissaoService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PermissaoControllerTest {
    @Mock
    PermissaoService permissaoService;
    @InjectMocks
    PermissaoController controller;

    private PermissaoResponseDto criarDTO(int id, String nome) {
        return new PermissaoResponseDto(id, nome);
    }

    @Test
    void listarTodos() {
        when(permissaoService.listarTodos())
                .thenReturn(List.of(criarDTO(1, "CRIAR"), criarDTO(2, "EDITAR"), criarDTO(3, "DELETAR")));

        ResponseEntity<List<PermissaoResponseDto>> resultado = controller.listarTodos();

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(3, resultado.getBody().size());
        verify(permissaoService).listarTodos();
    }

    @Test
    void buscarPorId() {
        when(permissaoService.buscarPorId(1))
                .thenReturn(criarDTO(1, "CRIAR"));

        ResponseEntity<PermissaoResponseDto> resultado = controller.buscarPorId(1);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals("CRIAR", resultado.getBody().getNome());
        verify(permissaoService).buscarPorId(1);
    }

    @Test
    void criar() {
        PermissaoRequestDto dto = new PermissaoRequestDto("VISUALIZAR");
        when(permissaoService.criar(any()))
                .thenReturn(criarDTO(4, "VISUALIZAR"));

        ResponseEntity<PermissaoResponseDto> resultado = controller.criar(dto);

        assertEquals(HttpStatus.CREATED, resultado.getStatusCode());
        assertEquals("VISUALIZAR", resultado.getBody().getNome());
        verify(permissaoService).criar(any());
    }

    @Test
    void atualizar() {
        PermissaoRequestDto dto = new PermissaoRequestDto("MODIFICAR");
        when(permissaoService.atualizar(anyInt(), any()))
                .thenReturn(criarDTO(1, "MODIFICAR"));

        ResponseEntity<PermissaoResponseDto> resultado = controller.atualizar(1, dto);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals("MODIFICAR", resultado.getBody().getNome());
        verify(permissaoService).atualizar(1, dto);
    }

    @Test
    void deletar() {
        doNothing().when(permissaoService).deletar(1);

        ResponseEntity<Void> resultado = controller.deletar(1);

        assertEquals(HttpStatus.NO_CONTENT, resultado.getStatusCode());
        verify(permissaoService).deletar(1);
    }
}


