package sptech.classicamoveis.Relatorio.PdfGenerator;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import org.junit.jupiter.api.Test;
import sptech.classicamoveis.Relatorio.dto.RelatorioVendaPorVendedorDto;
import sptech.classicamoveis.Relatorio.dto.RelatorioVendaVendedorItemDto;
import sptech.classicamoveis.Relatorio.dto.RelatorioVendaVendedorVendaDto;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RelatorioVendedorPdfGeneratorTest {
    private final RelatorioVendedorPdfGenerator generator = new RelatorioVendedorPdfGenerator();

    @Test
    void geraPdfComVendasEItens() throws Exception {
        RelatorioVendaPorVendedorDto dto = new RelatorioVendaPorVendedorDto();
        dto.setVendedorId(3);
        dto.setVendedorNome("Lucas Oliveira");
        dto.setCargo("VENDEDOR");
        dto.setSalario(2500.0);
        dto.setComissao(2);
        dto.setValorComissao(60.0);
        dto.setTotalVendido(3000.0);
        dto.setDataInicio(LocalDate.of(2026, 10, 1));
        dto.setDataFim(LocalDate.of(2026, 10, 15));
        RelatorioVendaVendedorItemDto item = new RelatorioVendaVendedorItemDto();
        item.setProdutoId(1); item.setSku("SKU-1"); item.setProdutoNome("Sofá");
        item.setQuantidade(2); item.setValorUnitario(1500.0); item.setTotalItem(3000.0);
        RelatorioVendaVendedorVendaDto venda = new RelatorioVendaVendedorVendaDto();
        venda.setVendaId(24); venda.setDataHoraVenda(LocalDateTime.of(2026, 10, 10, 14, 30));
        venda.setValorTotalVenda(3000.0); venda.setItens(List.of(item));
        dto.setVendas(List.of(venda));

        byte[] pdf = generator.gerarPdf(dto);
        assertNotNull(pdf);
        assertTrue(pdf.length > 100);
        try (PdfDocument documento = new PdfDocument(new PdfReader(new ByteArrayInputStream(pdf)))) {
            assertTrue(documento.getNumberOfPages() >= 1);
        }
    }

    @Test
    void geraPdfSemVendas() {
        RelatorioVendaPorVendedorDto dto = new RelatorioVendaPorVendedorDto();
        dto.setVendas(List.of());
        assertTrue(generator.gerarPdf(dto).length > 100);
    }
}
