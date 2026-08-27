package sptech.classicamoveis.Relatorio;
import org.junit.jupiter.api.Test;import org.junit.jupiter.api.extension.ExtendWith;import org.mockito.*;import org.mockito.junit.jupiter.MockitoExtension;import org.springframework.http.HttpStatus;import java.time.LocalDateTime;import java.util.*;import static org.junit.jupiter.api.Assertions.*;import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class) class RelatorioControllerTest{@Mock RelatorioService service;@InjectMocks RelatorioController controller;
@Test void fornecedor(){when(service.relatorioVendasPorFornecedor(1,2)).thenReturn(List.of());assertEquals(HttpStatus.OK,controller.relatorioVendasPorFornecedor(1,2).getStatusCode());}
@Test void produto(){when(service.relatorioVendasPorProduto(any(),any(),any(),any(),any())).thenReturn(List.of());assertEquals(HttpStatus.OK,controller.relatorioVendasPorProduto(1,2,List.of(3),LocalDateTime.now(),LocalDateTime.now()).getStatusCode());}}
