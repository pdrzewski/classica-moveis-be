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
import sptech.classicamoveis.Relatorio.dto.RelatorioTabelaPrecosDto;
import sptech.classicamoveis.Relatorio.dto.RelatorioTabelaPrecosItemDto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class RelatorioTabelaPrecosPdfGenerator {

    private final NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    private final DateTimeFormatter formatoDataHora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public byte[] gerarPdf(RelatorioTabelaPrecosDto relatorio) {

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

            direita.add(new Paragraph("TABELA DE PREÇOS E ESTOQUE").setFont(fonteNegrito).setFontSize(12));

            direita.add(new Paragraph("Por loja").setFont(fonteNormal).setFontSize(8));

            cabecalho.addCell(direita);

            document.add(cabecalho);

            // ==================================================
            // DADOS DA LOJA
            // ==================================================

            document.add(espaco());

            document.add(new Paragraph("DADOS DA LOJA").setFont(fonteNegrito).setFontSize(11));

            Table loja = new Table(UnitValue.createPercentArray(new float[]{1, 1}));

            loja.setWidth(UnitValue.createPercentValue(100));

            loja.setBorder(new SolidBorder(ColorConstants.BLACK, 0.7f));

            adicionarCampo(loja, "Loja:", relatorio.getEstabelecimento(), fonteNegrito, fonteNormal);

            adicionarCampo(loja, "Fornecedor:", relatorio.getFornecedor(), fonteNegrito, fonteNormal);

            adicionarCampo(loja, "Gerado em:", LocalDateTime.now().format(formatoDataHora), fonteNegrito, fonteNormal);

            adicionarCampo(loja, "Ordenação:", "alfabética pela descrição", fonteNegrito, fonteNormal);

            document.add(loja);

            // ==================================================
            // PRODUTOS
            // ==================================================

            document.add(espaco());

            document.add(new Paragraph("PRODUTOS").setFont(fonteNegrito).setFontSize(11));

            if (relatorio.getProdutos() == null || relatorio.getProdutos().isEmpty()) {

                document.add(new Paragraph("Nenhum produto encontrado para os filtros informados.").setFont(fonteNormal).setFontSize(9));

            } else {

                Table tabela = new Table(UnitValue.createPercentArray(new float[]{1.7f, 3.2f, 1.1f, 1.4f, 1.4f}));

                tabela.setWidth(UnitValue.createPercentValue(100));

                tabela.setBorder(new SolidBorder(ColorConstants.BLACK, 1));

                adicionarCabecalho(tabela, "CÓDIGO", fonteNegrito);

                adicionarCabecalho(tabela, "DESCRIÇÃO", fonteNegrito);

                adicionarCabecalho(tabela, "QT. ESTOQUE", fonteNegrito);

                adicionarCabecalho(tabela, "PREÇO DE COMPRA", fonteNegrito);

                adicionarCabecalho(tabela, "PREÇO DE VENDA", fonteNegrito);

                for (RelatorioTabelaPrecosItemDto produto : relatorio.getProdutos()) {

                    adicionarCelula(tabela, valorNulo(produto.getCodigoProduto()), fonteNormal);

                    adicionarCelula(tabela, valorNulo(produto.getDescricao()), fonteNormal);

                    adicionarCelula(tabela, String.valueOf(produto.getQuantidadeEstoque() == null ? 0 : produto.getQuantidadeEstoque()), fonteNormal);

                    adicionarCelula(tabela, formatarMoeda(produto.getPrecoCompra()), fonteNormal);

                    adicionarCelula(tabela, formatarMoeda(produto.getPrecoVenda()), fonteNormal);
                }

                document.add(tabela);
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
            throw new RuntimeException("Erro ao gerar PDF da tabela de preços e estoque", e);
        }
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