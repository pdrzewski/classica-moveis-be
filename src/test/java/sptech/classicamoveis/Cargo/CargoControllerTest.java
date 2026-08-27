package sptech.classicamoveis.Cargo.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sptech.classicamoveis.Cargo.dto.CargoRequestDto;
import sptech.classicamoveis.Cargo.dto.CargoResponseDto;
import sptech.classicamoveis.Cargo.service.CargoService;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CargoControllerTest {
    @Mock
    CargoService cargoService;
    @InjectMocks
    CargoController controller;

    private CargoResponseDto criarDTO(int id, String nome) {
        return new CargoResponseDto(id, nome, Set.of());
    }

    @Test
    void listarTodos() {
        when(cargoService.listarTodos())
                .thenReturn(List.of(criarDTO(1, "GERENTE"), criarDTO(2, "CAIXA")));

        ResponseEntity<List<CargoResponseDto>> resultado = controller.listarTodos();

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(2, resultado.getBody().size());
        verify(cargoService).listarTodos();
    }

    @Test
    void buscarPorId() {
        when(cargoService.buscarPorId(1))
                .thenReturn(criarDTO(1, "GERENTE"));

        ResponseEntity<CargoResponseDto> resultado = controller.buscarPorId(1);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals("GERENTE", resultado.getBody().getNome());
        verify(cargoService).buscarPorId(1);
    }

    @Test
    void criar() {
        CargoRequestDto dto = new CargoRequestDto("VENDEDOR", Set.of());
        when(cargoService.criar(any()))
                .thenReturn(criarDTO(3, "VENDEDOR"));

        ResponseEntity<CargoResponseDto> resultado = controller.criar(dto);

        assertEquals(HttpStatus.CREATED, resultado.getStatusCode());
        assertEquals("VENDEDOR", resultado.getBody().getNome());
        verify(cargoService).criar(any());
    }

    @Test
    void atualizar() {
        CargoRequestDto dto = new CargoRequestDto("GERENTE ATUALIZADO", Set.of());
        when(cargoService.atualizar(eq(1), any()))
                .thenReturn(criarDTO(1, "GERENTE ATUALIZADO"));

        ResponseEntity<CargoResponseDto> resultado = controller.atualizar(1, dto);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals("GERENTE ATUALIZADO", resultado.getBody().getNome());
        verify(cargoService).atualizar(1, dto);
    }

    @Test
    void deletar() {
        doNothing().when(cargoService).deletar(1);

        ResponseEntity<Void> resultado = controller.deletar(1);

        assertEquals(HttpStatus.NO_CONTENT, resultado.getStatusCode());
        verify(cargoService).deletar(1);
    }
}

