package sptech.classicamoveis.Estabelecimento;
import org.junit.jupiter.api.Test;import org.junit.jupiter.api.extension.ExtendWith;import org.mockito.*;import org.mockito.junit.jupiter.MockitoExtension;import org.springframework.http.HttpStatus;import sptech.classicamoveis.Estabelecimento.controller.EstabelecimentoController;import sptech.classicamoveis.Estabelecimento.dto.*;import sptech.classicamoveis.Estabelecimento.service.EstabelecimentoService;import java.util.*;import static org.junit.jupiter.api.Assertions.*;import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class) class EstabelecimentoControllerTest{@Mock EstabelecimentoService service;@InjectMocks EstabelecimentoController controller;
@Test void listar(){when(service.listarTodos()).thenReturn(List.of());assertEquals(HttpStatus.OK,controller.listarTodos().getStatusCode());}
@Test void buscar(){when(service.buscarPorId(1)).thenReturn(new EstabelecimentoResponseDto());assertEquals(HttpStatus.OK,controller.buscarPorId(1).getStatusCode());}
@Test void criar(){when(service.criar(any())).thenReturn(new EstabelecimentoResponseDto());assertEquals(HttpStatus.CREATED,controller.criar(new EstabelecimentoComEnderecoRequestDto()).getStatusCode());}
@Test void atualizar(){when(service.atualizar(eq(1),any())).thenReturn(new EstabelecimentoResponseDto());assertEquals(HttpStatus.OK,controller.atualizar(1,new EstabelecimentoRequestDto()).getStatusCode());}
@Test void deletar(){assertEquals(HttpStatus.NO_CONTENT,controller.deletar(1).getStatusCode());verify(service).deletar(1);}}
