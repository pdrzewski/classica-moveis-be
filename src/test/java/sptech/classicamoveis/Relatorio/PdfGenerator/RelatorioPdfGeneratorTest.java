package sptech.classicamoveis.Relatorio.PdfGenerator;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import org.junit.jupiter.api.Test;
import sptech.classicamoveis.Relatorio.dto.RelatorioVendaDto;
import sptech.classicamoveis.Relatorio.dto.RelatorioVendaPagamentoDto;
import sptech.classicamoveis.Relatorio.dto.RelatorioVendaProdutoDto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RelatorioPdfGeneratorTest {
    private final RelatorioPdfGenerator generator = new RelatorioPdfGenerator();

    @Test
    void geraPdfComRelatorioCompleto() throws Exception {
        RelatorioVendaDto dto = new RelatorioVendaDto();
        dto.setNumeroPedido(24);
        dto.setDataHoraGeracao(LocalDateTime.of(2026, 10, 15, 10, 30));
        dto.setNomeCliente("Carlos Eduardo Martins");
        dto.setDocumentoCliente("12345678900");
        dto.setTelefoneCliente("11999990000");
        dto.setEnderecoCompletoCliente("Rua A, 10 - Centro - São Paulo/SP - CEP 01000-000");
        dto.setNomeVendedor("Lucas Oliveira");
        dto.setProdutos(List.of(new RelatorioVendaProdutoDto("SKU-1", 2, "Sofá", 1500.0, 100.0, 2900.0)));
        dto.setPagamentos(List.of(new RelatorioVendaPagamentoDto("CREDITO", 2900.0, 3)));
        dto.setDesconto(100.0);
        dto.setTotalProdutos(2900.0);
        dto.setFrete(150.0);
        dto.setTotalPedido(3050.0);

        byte[] pdf = generator.gerarPdf(dto);

        assertNotNull(pdf);
        assertTrue(pdf.length > 100);
        try (PdfDocument documento = new PdfDocument(new PdfReader(new ByteArrayInputStream(pdf)))) {
            assertEquals(1, documento.getNumberOfPages());
        }
    }

    @Test
    void geraPdfQuandoCamposOpcionaisEstaoNulosEListasVazias() {
        RelatorioVendaDto dto = new RelatorioVendaDto();
        dto.setProdutos(null);
        dto.setPagamentos(List.of());

        byte[] pdf = assertDoesNotThrow(() -> generator.gerarPdf(dto));

        assertNotNull(pdf);
        assertTrue(pdf.length > 100);
    }
}
