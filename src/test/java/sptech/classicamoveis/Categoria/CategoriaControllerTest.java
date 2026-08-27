package sptech.classicamoveis.Categoria;
import org.junit.jupiter.api.Test;import org.junit.jupiter.api.extension.ExtendWith;import org.mockito.*;import org.mockito.junit.jupiter.MockitoExtension;import org.springframework.http.HttpStatus;import sptech.classicamoveis.Categoria.controller.CategoriaController;import sptech.classicamoveis.Categoria.dto.*;import sptech.classicamoveis.Categoria.service.CategoriaService;import java.util.*;import static org.junit.jupiter.api.Assertions.*;import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class) class CategoriaControllerTest{@Mock CategoriaService service;@InjectMocks CategoriaController controller;
@Test void listar(){when(service.listarTodos()).thenReturn(List.of());assertEquals(HttpStatus.OK,controller.listarTodos().getStatusCode());}
@Test void buscar(){when(service.buscarPorId(1)).thenReturn(new CategoriaResponseDto(1,"Sala"));assertEquals("Sala",controller.buscarPorId(1).getBody().getNome());}
@Test void criar(){when(service.criar(any())).thenReturn(new CategoriaResponseDto(1,"Sala"));assertEquals(HttpStatus.CREATED,controller.criar(new CategoriaRequestDto("Sala")).getStatusCode());}
@Test void atualizar(){when(service.atualizar(eq(1),any())).thenReturn(new CategoriaResponseDto(1,"Nova"));assertEquals("Nova",controller.atualizar(1,new CategoriaRequestDto("Nova")).getBody().getNome());}
@Test void deletar(){doNothing().when(service).deletar(1);assertEquals(HttpStatus.NO_CONTENT,controller.deletar(1).getStatusCode());verify(service).deletar(1);}}
