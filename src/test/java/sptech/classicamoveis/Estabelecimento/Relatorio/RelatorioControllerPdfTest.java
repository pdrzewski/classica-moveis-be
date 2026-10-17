package sptech.classicamoveis.Relatorio;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import sptech.classicamoveis.Relatorio.PdfGenerator.RelatorioPdfGenerator;
import sptech.classicamoveis.Relatorio.PdfGenerator.RelatorioTabelaPrecosPdfGenerator;
import sptech.classicamoveis.Relatorio.PdfGenerator.RelatorioVendedorPdfGenerator;
import sptech.classicamoveis.Relatorio.dto.RelatorioTabelaPrecosDto;
import sptech.classicamoveis.Relatorio.dto.RelatorioVendaDto;
import sptech.classicamoveis.Relatorio.dto.RelatorioVendaPorVendedorDto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/** Cobre os endpoints /pdf do RelatorioController e a montagem do nome do arquivo. */
@ExtendWith(MockitoExtension.class)
class RelatorioControllerPdfTest {

    @Mock RelatorioService service;
    @Mock RelatorioPdfGenerator vendaPdf;
    @Mock RelatorioTabelaPrecosPdfGenerator tabelaPdf;
    @Mock RelatorioVendedorPdfGenerator vendedorPdf;
    @InjectMocks RelatorioController controller;

    private final byte[] pdf = {1, 2, 3, 4};

    private String hoje() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    private void verificarPdf(ResponseEntity<byte[]> r, String nomeEsperado) {
        assertEquals(HttpStatus.OK, r.getStatusCode());
        assertArrayEquals(pdf, r.getBody());
        assertEquals(MediaType.APPLICATION_PDF, r.getHeaders().getContentType());
        assertEquals(pdf.length, r.getHeaders().getContentLength());
        assertEquals(nomeEsperado, r.getHeaders().getContentDisposition().getFilename());
        assertTrue(r.getHeaders().getContentDisposition().isAttachment());
    }

    @Test
    void pdfDoVendedorRemoveAcentosEPontuacaoDoNome() {
        RelatorioVendaPorVendedorDto dto = new RelatorioVendaPorVendedorDto();
        dto.setVendedorNome("José Antônio  da Silva!");
        when(service.gerarRelatorioVendaPorVendedor(1, null, null)).thenReturn(dto);
        when(vendedorPdf.gerarPdf(dto)).thenReturn(pdf);

        verificarPdf(controller.gerarRelatorioVendaPorVendedorPdf(1, null, null),
                "Relatorio-vendas-Jose-Antonio-da-Silva-" + hoje() + ".pdf");
    }

    @Test
    void pdfDoVendedorSemNomeOmiteONomeNoArquivo() {
        RelatorioVendaPorVendedorDto dto = new RelatorioVendaPorVendedorDto();
        dto.setVendedorNome(null);
        when(service.gerarRelatorioVendaPorVendedor(1, null, null)).thenReturn(dto);
        when(vendedorPdf.gerarPdf(dto)).thenReturn(pdf);

        verificarPdf(controller.gerarRelatorioVendaPorVendedorPdf(1, null, null),
                "Relatorio-vendas-" + hoje() + ".pdf");
    }

    @Test
    void pdfDoVendedorComNomeEmBrancoOmiteONomeNoArquivo() {
        RelatorioVendaPorVendedorDto dto = new RelatorioVendaPorVendedorDto();
        dto.setVendedorNome("   ");
        when(service.gerarRelatorioVendaPorVendedor(1, null, null)).thenReturn(dto);
        when(vendedorPdf.gerarPdf(dto)).thenReturn(pdf);

        verificarPdf(controller.gerarRelatorioVendaPorVendedorPdf(1, null, null),
                "Relatorio-vendas-" + hoje() + ".pdf");
    }

    @Test
    void pdfDaTabelaDePrecosUsaONomeDoEstabelecimento() {
        RelatorioTabelaPrecosDto dto = new RelatorioTabelaPrecosDto();
        dto.setEstabelecimento("Loja Centro");
        when(service.gerarTabelaPrecos(2, 5L)).thenReturn(dto);
        when(tabelaPdf.gerarPdf(dto)).thenReturn(pdf);

        verificarPdf(controller.gerarTabelaPrecosPdf(2, 5L),
                "Tabela-precos-estoque-Loja-Centro-" + hoje() + ".pdf");
    }

    @Test
    void pdfDaVendaUsaONumeroDoPedido() {
        RelatorioVendaDto dto = new RelatorioVendaDto();
        when(service.gerarRelatorioVenda(9)).thenReturn(dto);
        when(vendaPdf.gerarPdf(dto)).thenReturn(pdf);

        verificarPdf(controller.gerarRelatorioVendaPdf(9), "pedido-9.pdf");
    }
}
