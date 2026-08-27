package sptech.classicamoveis.Fornecedor.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sptech.classicamoveis.Fornecedor.dto.FornecedorComEnderecoRequestDTO;
import sptech.classicamoveis.Fornecedor.dto.FornecedorRequestDTO;
import sptech.classicamoveis.Fornecedor.dto.FornecedorResponseDTO;
import sptech.classicamoveis.Fornecedor.service.FornecedorService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FornecedorControllerTest {
    @Mock
    FornecedorService fornecedorService;
    @InjectMocks
    FornecedorController controller;

    private FornecedorResponseDTO criarDTO(long id, String nome) {
        return new FornecedorResponseDTO(id, nome, "12.345.678/0001-99", "Representante", "1133334444", "fornecedor@email.com", null);
    }

    @Test
    void listarTodos() {
        when(fornecedorService.listarTodos())
                .thenReturn(List.of(criarDTO(1L, "Fornecedor A"), criarDTO(2L, "Fornecedor B")));

        ResponseEntity<List<FornecedorResponseDTO>> resultado = controller.listarTodos();

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(2, resultado.getBody().size());
        verify(fornecedorService).listarTodos();
    }

    @Test
    void buscarPorId() {
        when(fornecedorService.buscarPorId(1L))
                .thenReturn(criarDTO(1L, "Fornecedor A"));

        ResponseEntity<FornecedorResponseDTO> resultado = controller.buscarPorId(1L);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals("Fornecedor A", resultado.getBody().nome());
        verify(fornecedorService).buscarPorId(1L);
    }

    @Test
    void criar() {
        FornecedorComEnderecoRequestDTO dto = new FornecedorComEnderecoRequestDTO(
            "Novo Fornecedor", "98765432000188", "Representante", "1144445555", null,
            "01310100", "Rua B", "Bairro", "São Paulo", "200", null, "SP"
        );

        when(fornecedorService.criar(any()))
                .thenReturn(criarDTO(3L, "Novo Fornecedor"));

        ResponseEntity<FornecedorResponseDTO> resultado = controller.criar(dto);

        assertEquals(HttpStatus.CREATED, resultado.getStatusCode());
        assertEquals("Novo Fornecedor", resultado.getBody().nome());
        verify(fornecedorService).criar(any());
    }

    @Test
    void atualizar() {
        FornecedorRequestDTO dto = new FornecedorRequestDTO(
            "Fornecedor A Atualizado", "12345678000199", "Representante", "1133334444", null, 1
        );

        when(fornecedorService.atualizar(anyLong(), any()))
                .thenReturn(criarDTO(1L, "Fornecedor A Atualizado"));

        ResponseEntity<FornecedorResponseDTO> resultado = controller.atualizar(1L, dto);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals("Fornecedor A Atualizado", resultado.getBody().nome());
        verify(fornecedorService).atualizar(1L, dto);
    }

    @Test
    void deletar() {
        doNothing().when(fornecedorService).deletar(1L);

        ResponseEntity<Void> resultado = controller.deletar(1L);

        assertEquals(HttpStatus.NO_CONTENT, resultado.getStatusCode());
        verify(fornecedorService).deletar(1L);
    }
}



