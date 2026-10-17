package sptech.classicamoveis.Cliente;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import sptech.classicamoveis.Cliente.controller.ClienteController;
import sptech.classicamoveis.Cliente.dto.*;
import sptech.classicamoveis.Cliente.service.ClienteService;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteControllerTest {
 @Mock ClienteService service;
 @InjectMocks ClienteController controller;
 @Test void listar(){when(service.listarClientes()).thenReturn(List.of()); assertEquals(HttpStatus.OK,controller.listarClientes().getStatusCode());}
 @Test void buscar(){ClienteResponseDto dto=new ClienteResponseDto(); when(service.buscarClientePorId(1)).thenReturn(dto); assertSame(dto,controller.buscarClientePorId(1).getBody());}
 @Test void nome(){when(service.buscarClientesPorNome("Ana")).thenReturn(List.of());assertEquals(HttpStatus.OK,controller.buscarClientesPorNome("Ana").getStatusCode());}
 @Test void documento(){when(service.buscarClientesPorDocumento("123")).thenReturn(List.of());assertEquals(HttpStatus.OK,controller.buscarClientesPorDocumento("123").getStatusCode());}
 @Test void criar(){ClienteResponseDto dto=new ClienteResponseDto();when(service.criarCliente(any())).thenReturn(dto);var response=controller.criarCliente(new ClienteComEnderecoRequestDto());assertEquals(HttpStatus.CREATED,response.getStatusCode());assertSame(dto,response.getBody());}
 @Test void atualizar(){ClienteResponseDto dto=new ClienteResponseDto();ClienteComEnderecoRequestDto request=new ClienteComEnderecoRequestDto();when(service.atualizarCliente(eq(1),same(request))).thenReturn(dto);assertSame(dto,controller.atualizarCliente(1,request).getBody());}
 @Test void deletar(){assertEquals(HttpStatus.NO_CONTENT,controller.deletarCliente(1).getStatusCode());verify(service).deletarCliente(1);}
}
