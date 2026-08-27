package sptech.classicamoveis.Cliente;
import org.junit.jupiter.api.Test;import org.junit.jupiter.api.extension.ExtendWith;import org.mockito.*;import org.mockito.junit.jupiter.MockitoExtension;import org.springframework.http.HttpStatus;import sptech.classicamoveis.Cliente.controller.ClienteController;import sptech.classicamoveis.Cliente.dto.*;import sptech.classicamoveis.Cliente.service.ClienteService;import java.util.*;import static org.junit.jupiter.api.Assertions.*;import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class) class ClienteControllerTest{@Mock ClienteService service;@InjectMocks ClienteController controller;
@Test void listar(){when(service.listarClientes()).thenReturn(List.of());assertEquals(HttpStatus.OK,controller.listarClientes().getStatusCode());}
@Test void buscar(){when(service.buscarClientePorId(1)).thenReturn(null);assertEquals(HttpStatus.OK,controller.buscarClientePorId(1).getStatusCode());}
@Test void nome(){when(service.buscarClientesPorNome("Ana")).thenReturn(List.of());assertEquals(HttpStatus.OK,controller.buscarClientesPorNome("Ana").getStatusCode());}
@Test void documento(){when(service.buscarClientesPorDocumento("123")).thenReturn(List.of());assertEquals(HttpStatus.OK,controller.buscarClientesPorDocumento("123").getStatusCode());}
@Test void criar(){when(service.criarCliente(any())).thenReturn(new ClienteResponseDto());assertEquals(HttpStatus.OK,controller.criarCliente(new ClienteComEnderecoRequestDto()).getStatusCode());}
@Test void atualizar(){Cliente c=new Cliente();when(service.atualizarCliente(eq(1),any())).thenReturn(c);assertSame(c,controller.atualizarCliente(1,c).getBody());}
@Test void deletar(){assertEquals(HttpStatus.NO_CONTENT,controller.deletarCliente(1).getStatusCode());verify(service).deletarCliente(1);}}
