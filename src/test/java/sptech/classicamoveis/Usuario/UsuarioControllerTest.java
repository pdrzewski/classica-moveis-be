package sptech.classicamoveis.Usuario.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sptech.classicamoveis.Usuario.dto.UsuarioRequestDto;
import sptech.classicamoveis.Usuario.dto.UsuarioResponseDto;
import sptech.classicamoveis.Usuario.service.UsuarioService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {
    @Mock
    UsuarioService usuarioService;
    @InjectMocks
    UsuarioController controller;

    private UsuarioResponseDto criarDTO(int id, String login) {
        return new UsuarioResponseDto(id, login);
    }

    @Test
    void listarTodos() {
        when(usuarioService.listarTodos())
                .thenReturn(List.of(criarDTO(1, "user1"), criarDTO(2, "user2")));

        ResponseEntity<List<UsuarioResponseDto>> resultado = controller.listarTodos();

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(2, resultado.getBody().size());
        verify(usuarioService).listarTodos();
    }

    @Test
    void buscarPorId() {
        when(usuarioService.buscarPorId(1))
                .thenReturn(criarDTO(1, "user1"));

        ResponseEntity<UsuarioResponseDto> resultado = controller.buscarPorId(1);

        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals("user1", resultado.getBody().getLogin());
        verify(usuarioService).buscarPorId(1);
    }

    @Test
    void criar() {
        UsuarioRequestDto dto = new UsuarioRequestDto("newuser", "senha123");
        when(usuarioService.criar(any()))
                .thenReturn(criarDTO(3, "newuser"));

        ResponseEntity<UsuarioResponseDto> resultado = controller.criar(dto);

        assertEquals(HttpStatus.CREATED, resultado.getStatusCode());
        assertEquals("newuser", resultado.getBody().getLogin());
        verify(usuarioService).criar(any());
    }

    @Test
    void deletar() {
        doNothing().when(usuarioService).deletar(1);

        ResponseEntity<Void> resultado = controller.deletar(1);

        assertEquals(HttpStatus.NO_CONTENT, resultado.getStatusCode());
        verify(usuarioService).deletar(1);
    }
}



