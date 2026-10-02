package sptech.classicamoveis.Relatorio.PdfGenerator;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.stereotype.Component;
import sptech.classicamoveis.Relatorio.dto.RelatorioVendaPorVendedorDto;
import sptech.classicamoveis.Relatorio.dto.RelatorioVendaVendedorItemDto;
import sptech.classicamoveis.Relatorio.dto.RelatorioVendaVendedorVendaDto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class RelatorioVendedorPdfGenerator {

    private final NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    private final DateTimeFormatter formatoDataHora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public byte[] gerarPdf(RelatorioVendaPorVendedorDto relatorio) {

        try {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            document.setMargins(25, 25, 25, 25);

            // ==================================================
            // FONTES
            // ==================================================

            PdfFont fonteNormal = PdfFontFactory.createFont(StandardFonts.HELVETICA);

            PdfFont fonteNegrito = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

            // ==================================================
            // CABEÇALHO
            // ==================================================

            Table cabecalho = new Table(UnitValue.createPercentArray(new float[]{3, 2}));

            cabecalho.setWidth(UnitValue.createPercentValue(100));

            cabecalho.setBorder(new SolidBorder(ColorConstants.BLACK, 1));

            Cell esquerda = new Cell();

            esquerda.setBorder(Border.NO_BORDER);

            esquerda.add(new Paragraph("CLASSICA").setFont(fonteNegrito).setFontSize(18));

            esquerda.add(new Paragraph("MÓVEIS E COLCHÕES").setFont(fonteNormal).setFontSize(8));

            cabecalho.addCell(esquerda);

            Cell direita = new Cell();

            direita.setBorder(Border.NO_BORDER);

            direita.setTextAlignment(TextAlignment.RIGHT);

            try {

                var recurso = getClass().getResource("/logo/classica.png");

                if (recurso != null) {

                    ImageData imagem = ImageDataFactory.create(recurso);

                    Image logo = new Image(imagem);

                    logo.scaleToFit(150, 60);

                    direita.add(logo);
                }

            } catch (Exception e) {
            }

            direita.add(new Paragraph("RELATÓRIO DE VENDAS").setFont(fonteNegrito).setFontSize(12));

            direita.add(new Paragraph("Por vendedor").setFont(fonteNormal).setFontSize(8));

            cabecalho.addCell(direita);

            document.add(cabecalho);

            // ==================================================
            // DADOS DO VENDEDOR
            // ==================================================

            document.add(espaco());

            document.add(new Paragraph("DADOS DO VENDEDOR").setFont(fonteNegrito).setFontSize(11));

            Table vendedor = new Table(UnitValue.createPercentArray(new float[]{1, 1}));

            vendedor.setWidth(UnitValue.createPercentValue(100));

            vendedor.setBorder(new SolidBorder(ColorConstants.BLACK, 0.7f));

            adicionarCampo(vendedor, "Vendedor:", relatorio.getVendedorNome(), fonteNegrito, fonteNormal);

            adicionarCampo(vendedor, "ID:", String.valueOf(relatorio.getVendedorId()), fonteNegrito, fonteNormal);

            adicionarCampo(vendedor, "Cargo:", relatorio.getCargo(), fonteNegrito, fonteNormal);

            adicionarCampo(vendedor, "Salário:", formatarMoeda(relatorio.getSalario()), fonteNegrito, fonteNormal);

            adicionarCampo(vendedor, "Comissão:", relatorio.getComissao() + "%", fonteNegrito, fonteNormal);

            Cell periodo = new Cell(1, 2);

            periodo.setBorder(Border.NO_BORDER);

            periodo.add(new Paragraph().add(new Text("Período: ").setFont(fonteNegrito)).add(formatarPeriodo(relatorio)).setFont(fonteNormal).setFontSize(9));

            vendedor.addCell(periodo);

            document.add(vendedor);

            // ==================================================
            // RESUMO
            // ==================================================

            document.add(espaco());

            document.add(new Paragraph("RESUMO").setFont(fonteNegrito).setFontSize(11));

            Table resumo = new Table(UnitValue.createPercentArray(new float[]{1, 1, 1}));

            resumo.setWidth(UnitValue.createPercentValue(100));

            adicionarResumo(resumo, "QUANTIDADE DE VENDAS", String.valueOf(relatorio.getVendas() == null ? 0 : relatorio.getVendas().size()), fonteNegrito, fonteNormal);

            adicionarResumo(resumo, "TOTAL VENDIDO", formatarMoeda(relatorio.getTotalVendido()), fonteNegrito, fonteNormal);

            adicionarResumo(resumo, "VALOR DA COMISSÃO", formatarMoeda(relatorio.getValorComissao()), fonteNegrito, fonteNormal);

            document.add(resumo);

            // ==================================================
            // VENDAS
            // ==================================================

            document.add(espaco());

            document.add(new Paragraph("VENDAS REALIZADAS").setFont(fonteNegrito).setFontSize(11));

            if (relatorio.getVendas() == null || relatorio.getVendas().isEmpty()) {

                document.add(new Paragraph("Nenhuma venda encontrada no período.").setFont(fonteNormal).setFontSize(9));

            } else {

                for (RelatorioVendaVendedorVendaDto venda : relatorio.getVendas()) {

                    adicionarVenda(
                            document,
                            venda,
                            fonteNormal,
                            fonteNegrito
                    );}

            }

            // ==================================================
            // RODAPÉ
            // ==================================================

            document.add(espaco());

            document.add(new Paragraph("CLASSICA MÓVEIS")
                    .setFont(fonteNegrito)
                    .setFontSize(8)
                    .setTextAlignment(TextAlignment.CENTER));

            document.close();

            return outputStream.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Erro ao gerar PDF do relatório de vendas por vendedor", e);
        }
    }

    private void adicionarVenda(
            Document document,
            RelatorioVendaVendedorVendaDto venda,
            PdfFont fonteNormal,
            PdfFont fonteNegrito
    ) {

        Table tabelaVenda = new Table(UnitValue.createPercentArray(new float[]{1, 2, 2}));

        tabelaVenda.setWidth(UnitValue.createPercentValue(100));

        tabelaVenda.setBorder(new SolidBorder(ColorConstants.BLACK, 1));

        adicionarCabecalho(tabelaVenda, "VENDA", fonteNegrito);

        adicionarCabecalho(tabelaVenda, "DATA", fonteNegrito);

        adicionarCabecalho(tabelaVenda, "TOTAL", fonteNegrito);

        adicionarCelula(tabelaVenda, "#" + venda.getVendaId(), fonteNormal);

        adicionarCelula(tabelaVenda, venda.getDataHoraVenda() == null ? "-" : venda.getDataHoraVenda().format(formatoDataHora), fonteNormal);

        adicionarCelula(tabelaVenda, formatarMoeda(venda.getValorTotalVenda()), fonteNormal);

        document.add(tabelaVenda);

        if (venda.getItens() != null && !venda.getItens().isEmpty()) {
            Table tabelaItens = new Table(
                    UnitValue.createPercentArray(
                            new float[]{
                                    1.6f,
                                    3.0f,
                                    0.8f,
                                    1.3f,
                                    1.3f
                            }));

            tabelaItens.setWidth(UnitValue.createPercentValue(100));

            adicionarCabecalho(tabelaItens, "CÓDIGO ", fonteNegrito);

            adicionarCabecalho(tabelaItens, "PRODUTO", fonteNegrito);

            adicionarCabecalho(tabelaItens, "QT.", fonteNegrito);

            adicionarCabecalho(tabelaItens, "UNITÁRIO", fonteNegrito);

            adicionarCabecalho(tabelaItens, "TOTAL", fonteNegrito);

            for (RelatorioVendaVendedorItemDto item : venda.getItens()) {

                adicionarCelula(tabelaItens, valorNulo(item.getSku()), fonteNormal);

                adicionarCelula(tabelaItens, valorNulo(item.getProdutoNome()), fonteNormal);

                adicionarCelula(tabelaItens, String.valueOf(item.getQuantidade()), fonteNormal);

                adicionarCelula(tabelaItens, formatarMoeda(item.getValorUnitario()), fonteNormal);

                adicionarCelula(tabelaItens, formatarMoeda(item.getTotalItem()), fonteNormal);
            }

            document.add(tabelaItens);
        }

        document.add(espaco());
    }

    private String formatarPeriodo(RelatorioVendaPorVendedorDto relatorio) {

        if (relatorio.getDataInicio() == null || relatorio.getDataFim() == null) {return "-";}

        return relatorio.getDataInicio().format(formatoData) + " até " + relatorio.getDataFim().format(formatoData);
    }

    private void adicionarResumo(
            Table tabela,
            String titulo,
            String valor,
            PdfFont fonteNegrito,
            PdfFont fonteNormal
    ) {

        Cell celula = new Cell();

        celula.setTextAlignment(TextAlignment.CENTER);

        celula.setBorder(new SolidBorder(ColorConstants.BLACK, 0.7f));

        celula.add(new Paragraph(titulo).setFont(fonteNegrito).setFontSize(7));

        celula.add(new Paragraph(valor).setFont(fonteNormal).setFontSize(10));

        tabela.addCell(celula);
    }

    private void adicionarCabecalho(
            Table tabela,
            String texto,
            PdfFont fonte
    ) {

        Cell celula = new Cell();

        celula.setBackgroundColor(ColorConstants.LIGHT_GRAY);

        celula.setBorder(new SolidBorder(ColorConstants.BLACK, 0.5f));

        celula.setTextAlignment(TextAlignment.CENTER);

        celula.add(new Paragraph(texto).setFont(fonte).setFontSize(7));

        tabela.addCell(celula);
    }

    private void adicionarCelula(
            Table tabela,
            String texto,
            PdfFont fonte
    ) {

        Cell celula = new Cell();

        celula.setBorder(new SolidBorder(ColorConstants.BLACK, 0.5f));

        celula.add(new Paragraph(texto).setFont(fonte).setFontSize(7));

        tabela.addCell(celula);
    }

    private void adicionarCampo(
            Table tabela,
            String nome,
            String valor,
            PdfFont fonteNegrito,
            PdfFont fonteNormal
    ) {

        Cell celula = new Cell();

        celula.setBorder(Border.NO_BORDER);

        celula.add(new Paragraph()
                .add(new Text(nome + " ").setFont(fonteNegrito))
                .add(valorNulo(valor)).setFont(fonteNormal).setFontSize(9));

        tabela.addCell(celula);
    }

    private String formatarMoeda(Double valor) {

        if (valor == null) {
            valor = 0.0;
        }

        return formatoMoeda.format(valor);
    }

    private String valorNulo(String valor) {

        if (
                valor == null
                        ||
                        valor.isBlank()
        ) {
            return "-";
        }

        return valor;
    }

    private Paragraph espaco() {

        return new Paragraph(" ")
                .setFontSize(3);
    }
}