package sptech.classicamoveis.Relatorio.PdfGenerator;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import org.junit.jupiter.api.Test;
import sptech.classicamoveis.Relatorio.dto.RelatorioTabelaPrecosDto;
import sptech.classicamoveis.Relatorio.dto.RelatorioTabelaPrecosItemDto;

import java.io.ByteArrayInputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RelatorioTabelaPrecosPdfGeneratorTest {
    private final RelatorioTabelaPrecosPdfGenerator generator = new RelatorioTabelaPrecosPdfGenerator();

    @Test
    void geraPdfComProdutos() throws Exception {
        RelatorioTabelaPrecosDto dto = new RelatorioTabelaPrecosDto();
        dto.setEstabelecimento("Matriz");
        dto.setFornecedor("Fornecedor A");
        dto.setProdutos(List.of(new RelatorioTabelaPrecosItemDto("SKU-1", "Sofá", 8L, 900.0, 1500.0)));

        byte[] pdf = generator.gerarPdf(dto);

        assertNotNull(pdf);
        assertTrue(pdf.length > 100);
        try (PdfDocument documento = new PdfDocument(new PdfReader(new ByteArrayInputStream(pdf)))) {
            assertTrue(documento.getNumberOfPages() >= 1);
        }
    }

    @Test
    void geraPdfSemProdutos() {
        RelatorioTabelaPrecosDto dto = new RelatorioTabelaPrecosDto();
        dto.setEstabelecimento("Matriz");
        dto.setFornecedor("Todos os fornecedores");
        dto.setProdutos(List.of());
        assertTrue(generator.gerarPdf(dto).length > 100);
    }

    @Test
    void geraPdfComListaNula() {
        RelatorioTabelaPrecosDto dto = new RelatorioTabelaPrecosDto();
        dto.setProdutos(null);
        assertTrue(generator.gerarPdf(dto).length > 100);
    }
}
