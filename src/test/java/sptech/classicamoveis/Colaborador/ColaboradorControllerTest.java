package sptech.classicamoveis.Colaborador.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sptech.classicamoveis.Colaborador.AniversarioColaboradorDto;
import sptech.classicamoveis.Colaborador.dto.ColaboradorResponseDto;
import sptech.classicamoveis.Colaborador.service.ColaboradorService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ColaboradorControllerTest {
    @Mock
    ColaboradorService colaboradorService;
    @InjectMocks
    ColaboradorController controller;

    @Test
    void listarTodos() {
        when(colaboradorService.listarTodos())
                .thenReturn(List.of());

        ResponseEntity<List<ColaboradorResponseDto>> resultado = controller.listarTodos();

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertNotNull(resultado.getBody());
        verify(colaboradorService).listarTodos();
    }

    @Test
    void buscarPorId() {
        when(colaboradorService.buscarPorId(1))
                .thenReturn(null);

        ResponseEntity<ColaboradorResponseDto> resultado = controller.buscarPorId(1);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        verify(colaboradorService).buscarPorId(1);
    }

    @Test
    void aniversariosProximos() {
        when(colaboradorService.buscarAniversariosProximos(30))
                .thenReturn(List.of());

        ResponseEntity<List<AniversarioColaboradorDto>> resultado = controller.aniversariosProximos(30);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(0, resultado.getBody().size());
        verify(colaboradorService).buscarAniversariosProximos(30);
    }
}


