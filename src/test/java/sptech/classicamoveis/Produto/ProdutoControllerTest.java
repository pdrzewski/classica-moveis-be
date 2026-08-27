package sptech.classicamoveis.Produto.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sptech.classicamoveis.Produto.dto.ProdutoRequestDTO;
import sptech.classicamoveis.Produto.dto.ProdutoResponseDTO;
import sptech.classicamoveis.Produto.service.ProdutoService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoControllerTest {
    @Mock
    ProdutoService produtoService;
    @InjectMocks
    ProdutoController controller;

    private ProdutoResponseDTO criarDTO(int id, String nome) {
        return new ProdutoResponseDTO(id, 1L, 1, nome, "SKU", "12345", "UN", "MARCA", 50.0, 100.0, 10, true);
    }

    @Test
    void listarTodos() {
        when(produtoService.listarTodos())
                .thenReturn(List.of(criarDTO(1, "Cadeira"), criarDTO(2, "Mesa")));

        ResponseEntity<List<ProdutoResponseDTO>> resultado = controller.listarTodos();

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(2, resultado.getBody().size());
        verify(produtoService).listarTodos();
    }

    @Test
    void buscarPorId() {
        when(produtoService.buscarPorId(1))
                .thenReturn(criarDTO(1, "Cadeira"));

        ResponseEntity<ProdutoResponseDTO> resultado = controller.buscarPorId(1);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals("Cadeira", resultado.getBody().nome());
        verify(produtoService).buscarPorId(1);
    }

    @Test
    void buscarPorTermo() {
        when(produtoService.buscarPorTermo("Cadeira"))
                .thenReturn(List.of(criarDTO(1, "Cadeira de Madeira")));

        ResponseEntity<List<ProdutoResponseDTO>> resultado = controller.buscarPorTermo("Cadeira");

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(1, resultado.getBody().size());
        verify(produtoService).buscarPorTermo("Cadeira");
    }

    @Test
    void buscarPorTermoVazio() {
        when(produtoService.buscarPorTermo(null))
                .thenReturn(List.of());

        ResponseEntity<List<ProdutoResponseDTO>> resultado = controller.buscarPorTermo(null);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        verify(produtoService).buscarPorTermo(null);
    }

    @Test
    void criar() {
        ProdutoRequestDTO dto = new ProdutoRequestDTO(1L, 1, "Poltrona", "SKU", "12345", "UN", "MARCA", 75.0, 150.0, 5, true);
        when(produtoService.criar(any()))
                .thenReturn(criarDTO(3, "Poltrona"));

        ResponseEntity<ProdutoResponseDTO> resultado = controller.criar(dto);

        assertEquals(HttpStatus.CREATED, resultado.getStatusCode());
        assertEquals("Poltrona", resultado.getBody().nome());
        verify(produtoService).criar(any());
    }

    @Test
    void atualizar() {
        ProdutoRequestDTO dto = new ProdutoRequestDTO(1L, 1, "Cadeira de Madeira", "SKU", "12345", "UN", "MARCA", 60.0, 120.0, 15, true);
        when(produtoService.atualizar(eq(1), any()))
                .thenReturn(criarDTO(1, "Cadeira de Madeira"));

        ResponseEntity<ProdutoResponseDTO> resultado = controller.atualizar(1, dto);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals("Cadeira de Madeira", resultado.getBody().nome());
        verify(produtoService).atualizar(1, dto);
    }

    @Test
    void deletar() {
        doNothing().when(produtoService).deletar(1);

        ResponseEntity<Void> resultado = controller.deletar(1);

        assertEquals(HttpStatus.NO_CONTENT, resultado.getStatusCode());
        verify(produtoService).deletar(1);
    }
}




