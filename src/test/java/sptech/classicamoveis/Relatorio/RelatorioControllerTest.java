package sptech.classicamoveis.Relatorio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import sptech.classicamoveis.Relatorio.PdfGenerator.RelatorioPdfGenerator;
import sptech.classicamoveis.Relatorio.PdfGenerator.RelatorioTabelaPrecosPdfGenerator;
import sptech.classicamoveis.Relatorio.PdfGenerator.RelatorioVendedorPdfGenerator;
import sptech.classicamoveis.Relatorio.dto.RelatorioVendaPorVendedorDto;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RelatorioControllerTest {
 @Mock RelatorioService service;
 @Mock RelatorioPdfGenerator vendaPdf;
 @Mock RelatorioTabelaPrecosPdfGenerator tabelaPdf;
 @Mock RelatorioVendedorPdfGenerator vendedorPdf;
 @InjectMocks RelatorioController controller;
 @Test void relatorioVendedor(){RelatorioVendaPorVendedorDto dto=new RelatorioVendaPorVendedorDto();when(service.gerarRelatorioVendaPorVendedor(1,null,null)).thenReturn(dto);var response=controller.gerarRelatorioVendaPorVendedor(1,null,null);assertEquals(HttpStatus.OK,response.getStatusCode());assertSame(dto,response.getBody());}
 @Test void relatorioVenda(){var dto=mock(sptech.classicamoveis.Relatorio.dto.RelatorioVendaDto.class);when(service.gerarRelatorioVenda(9)).thenReturn(dto);var response=controller.gerarRelatorioVenda(9);assertEquals(HttpStatus.OK,response.getStatusCode());assertSame(dto,response.getBody());}
 @Test void tabelaDePrecos(){var dto=new sptech.classicamoveis.Relatorio.dto.RelatorioTabelaPrecosDto();when(service.gerarTabelaPrecos(2,null)).thenReturn(dto);assertSame(dto,controller.gerarTabelaPrecos(2,null).getBody());}
}
