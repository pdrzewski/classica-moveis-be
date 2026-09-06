package sptech.classicamoveis.Relatorio;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Component
public class RelatorioPdfGenerator {

    private static final DateTimeFormatter DATA_HORA_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private static final Font FONTE_TITULO = new Font(Font.HELVETICA, 16, Font.BOLD);
    private static final Font FONTE_CABECALHO = new Font(Font.HELVETICA, 10, Font.BOLD, Color.WHITE);
    private static final Font FONTE_CELULA = new Font(Font.HELVETICA, 10);
    private static final Font FONTE_AVISO = new Font(Font.HELVETICA, 10, Font.ITALIC);

    public byte[] gerarRelatorioVendasPorFornecedor(List<RelatorioVendaItemDto> itens) {
        Document document = new Document(PageSize.A4.rotate(), 30, 30, 40, 30);
        ByteArrayOutputStream saida = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, saida);
            document.open();

            document.add(new Paragraph("Relatório de Vendas por Fornecedor", FONTE_TITULO));
            document.add(new Paragraph(" "));

            if (itens.isEmpty()) {
                document.add(new Paragraph("Nenhum registro encontrado para os filtros informados.", FONTE_AVISO));
            } else {
                PdfPTable tabela = new PdfPTable(new float[]{1.5f, 2.5f, 2f, 3f, 1.2f, 1.8f, 1.8f});
                tabela.setWidthPercentage(100);
                adicionarCabecalho(tabela, "Venda", "Data/Hora", "Loja", "Produto", "Qtd", "Preço Unit.", "Subtotal");

                for (RelatorioVendaItemDto item : itens) {
                    tabela.addCell(celula(String.valueOf(item.idVenda())));
                    tabela.addCell(celula(item.dataHora() != null ? item.dataHora().format(DATA_HORA_FORMATTER) : ""));
                    tabela.addCell(celula(item.nomeLoja()));
                    tabela.addCell(celula(item.nomeProduto()));
                    tabela.addCell(celula(item.quantidade() != null ? String.valueOf(item.quantidade()) : ""));
                    tabela.addCell(celula(formatarMoeda(item.precoUnitario())));
                    tabela.addCell(celula(formatarMoeda(item.subtotal())));
                }

                document.add(tabela);
            }

            document.close();
        } catch (DocumentException e) {
            throw new IllegalStateException("Erro ao gerar PDF do relatório de vendas por fornecedor", e);
        }

        return saida.toByteArray();
    }

    public byte[] gerarRelatorioVendasPorProduto(List<RelatorioVendasPorProdutoDto> itens) {
        Document document = new Document(PageSize.A4, 30, 30, 40, 30);
        ByteArrayOutputStream saida = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, saida);
            document.open();

            document.add(new Paragraph("Relatório de Vendas por Produto", FONTE_TITULO));
            document.add(new Paragraph(" "));

            if (itens.isEmpty()) {
                document.add(new Paragraph("Nenhum registro encontrado para os filtros informados.", FONTE_AVISO));
            } else {
                PdfPTable tabela = new PdfPTable(new float[]{1.2f, 3f, 2f, 2f});
                tabela.setWidthPercentage(100);
                adicionarCabecalho(tabela, "Produto ID", "Produto", "Qtd. Vendida", "Valor Total");

                for (RelatorioVendasPorProdutoDto item : itens) {
                    tabela.addCell(celula(String.valueOf(item.produtoId())));
                    tabela.addCell(celula(item.nomeProduto()));
                    tabela.addCell(celula(item.quantidadeVendida() != null ? String.valueOf(item.quantidadeVendida()) : ""));
                    tabela.addCell(celula(formatarMoeda(item.valorTotalVendido())));
                }

                document.add(tabela);
            }

            document.close();
        } catch (DocumentException e) {
            throw new IllegalStateException("Erro ao gerar PDF do relatório de vendas por produto", e);
        }

        return saida.toByteArray();
    }

    private void adicionarCabecalho(PdfPTable tabela, String... colunas) {
        for (String coluna : colunas) {
            PdfPCell celula = new PdfPCell(new Phrase(coluna, FONTE_CABECALHO));
            celula.setBackgroundColor(new Color(51, 51, 51));
            celula.setPadding(6f);
            tabela.addCell(celula);
        }
    }

    private PdfPCell celula(String texto) {
        PdfPCell celula = new PdfPCell(new Phrase(texto != null ? texto : "", FONTE_CELULA));
        celula.setPadding(5f);
        return celula;
    }

    private String formatarMoeda(Double valor) {
        return String.format(Locale.of("pt", "BR"), "R$ %.2f", valor != null ? valor : 0.0);
    }
}