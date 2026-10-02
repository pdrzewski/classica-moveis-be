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
import sptech.classicamoveis.Relatorio.dto.RelatorioVendaDto;
import sptech.classicamoveis.Relatorio.dto.RelatorioVendaPagamentoDto;
import sptech.classicamoveis.Relatorio.dto.RelatorioVendaProdutoDto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class RelatorioPdfGenerator {

    private final NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    private final DateTimeFormatter formatoDataHora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public byte[] gerarPdf(RelatorioVendaDto relatorio) {

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

            direita.add(new Paragraph("N. Pedido: " + relatorio.getNumeroPedido()).setFont(fonteNegrito).setFontSize(12));

            if (relatorio.getDataHoraGeracao() != null) {

                direita.add(new Paragraph(relatorio.getDataHoraGeracao().format(formatoDataHora)).setFont(fonteNormal).setFontSize(8));
            }

            cabecalho.addCell(direita);

            document.add(cabecalho);

            // ==================================================
            // DADOS DO CLIENTE
            // ==================================================

            document.add(espaco());

            document.add(new Paragraph("DADOS DO CLIENTE").setFont(fonteNegrito).setFontSize(11));

            Table cliente = new Table(UnitValue.createPercentArray(new float[]{1, 1}));

            cliente.setWidth(UnitValue.createPercentValue(100));

            cliente.setBorder(new SolidBorder(ColorConstants.BLACK, 0.7f));

            adicionarCampo(cliente, "Cliente:", relatorio.getNomeCliente(), fonteNegrito, fonteNormal);

            adicionarCampo(cliente, "CPF/CNPJ:", relatorio.getDocumentoCliente(), fonteNegrito, fonteNormal);

            adicionarCampo(cliente, "Telefone:", relatorio.getTelefoneCliente(), fonteNegrito, fonteNormal);

            adicionarCampo(cliente, "Vendedor:", relatorio.getNomeVendedor(), fonteNegrito, fonteNormal);

            Cell endereco = new Cell(1, 2);

            endereco.setBorder(Border.NO_BORDER);

            endereco.add(new Paragraph().add(new Text("Endereço: ").setFont(fonteNegrito)).add(valorNulo(relatorio.getEnderecoCompletoCliente())).setFont(fonteNormal).setFontSize(9));

            cliente.addCell(endereco);

            document.add(cliente);

            // ==================================================
            // PRODUTOS
            // ==================================================

            document.add(espaco());

            document.add(new Paragraph("PRODUTOS").setFont(fonteNegrito).setFontSize(11));

            Table tabelaProdutos = new Table(UnitValue.createPercentArray(new float[]{1.6f, 0.6f, 3.0f, 1.2f, 1.2f}));

            tabelaProdutos.setWidth(UnitValue.createPercentValue(100));

            tabelaProdutos.setBorder(new SolidBorder(ColorConstants.BLACK, 1));

            adicionarCabecalho(tabelaProdutos, "CÓDIGO", fonteNegrito);

            adicionarCabecalho(tabelaProdutos, "QT.", fonteNegrito);

            adicionarCabecalho(tabelaProdutos, "PRODUTO", fonteNegrito);

            adicionarCabecalho(tabelaProdutos, "PREÇO UNIT.", fonteNegrito);

            adicionarCabecalho(tabelaProdutos, "TOTAL", fonteNegrito);

            if (relatorio.getProdutos() != null) {

                for (RelatorioVendaProdutoDto produto : relatorio.getProdutos()) {

                    adicionarCelula(tabelaProdutos, valorNulo(produto.getCodigoProduto()), fonteNormal);

                    adicionarCelula(tabelaProdutos, String.valueOf(produto.getQuantidade()), fonteNormal);

                    adicionarCelula(tabelaProdutos, valorNulo(produto.getNomeProduto()), fonteNormal);

                    adicionarCelula(tabelaProdutos, formatarMoeda(produto.getPrecoUnitario()), fonteNormal);

                    // Total já calculado pelo Service, incluindo desconto
                    adicionarCelula(tabelaProdutos, formatarMoeda(produto.getTotal()), fonteNormal);
                }
            }

            document.add(tabelaProdutos);

            // ==================================================
            // TOTAIS
            // ==================================================

            document.add(espaco());

            Table tabelaTotais = new Table(UnitValue.createPercentArray(new float[]{3, 1}));

            tabelaTotais.setWidth(UnitValue.createPercentValue(100));

            tabelaTotais.setBorder(Border.NO_BORDER);

            adicionarTotal(tabelaTotais, "Desconto:", formatarMoeda(relatorio.getDesconto()), fonteNegrito, fonteNormal);

            adicionarTotal(tabelaTotais, "Total dos produtos:", formatarMoeda(relatorio.getTotalProdutos()), fonteNegrito, fonteNormal);

            adicionarTotal(tabelaTotais, "Frete:", formatarMoeda(relatorio.getFrete()), fonteNegrito, fonteNormal);

            adicionarTotal(tabelaTotais, "TOTAL:", formatarMoeda(relatorio.getTotalPedido()), fonteNegrito, fonteNormal);

            document.add(tabelaTotais);

            // ==================================================
            // FORMAS DE PAGAMENTO
            // ==================================================

            document.add(espaco());

            document.add(new Paragraph("FORMA DE PAGAMENTO").setFont(fonteNegrito).setFontSize(11));

            if (relatorio.getPagamentos() != null && !relatorio.getPagamentos().isEmpty()) {

                for (RelatorioVendaPagamentoDto pagamento : relatorio.getPagamentos()) {

                    document.add(new Paragraph(traduzirPagamento(pagamento.getFormaPagamento()) + "    " + formatarMoeda(pagamento.getValor())).setFont(fonteNormal).setFontSize(9));

                    Integer parcelas = pagamento.getQuantidadeParcelas();

                    if (parcelas != null && parcelas > 1) {

                        double valorParcela = pagamento.getValor() / parcelas;

                        document.add(new Paragraph("    " + parcelas + " parcelas de " + formatarMoeda(valorParcela)).setFont(fonteNormal).setFontSize(9));
                    }
                }

            } else {

                document.add(new Paragraph("Nenhum pagamento registrado.").setFont(fonteNormal).setFontSize(9));
            }

            // ==================================================
            // ENDEREÇO DE ENTREGA
            // ==================================================

            document.add(espaco());

            document.add(new Paragraph("ENDEREÇO DE ENTREGA").setFont(fonteNegrito).setFontSize(11));

            document.add(new Paragraph(valorNulo(relatorio.getEnderecoEntrega())).setFont(fonteNormal).setFontSize(9));

            if (relatorio.getDataHoraEntrega() != null) {

                document.add(new Paragraph("Data da entrega: " + relatorio.getDataHoraEntrega().format(formatoDataHora)).setFont(fonteNormal).setFontSize(9));
            }

            // ==================================================
            // AVISOS
            // ==================================================

            document.add(espaco());

            Table avisos = new Table(1);

            avisos.setWidth(UnitValue.createPercentValue(100));

            avisos.setBorder(new SolidBorder(ColorConstants.LIGHT_GRAY, 0.8f));

            Cell aviso1 = new Cell();

            aviso1.setBorder(Border.NO_BORDER);

            aviso1.add(new Paragraph("Qualquer reclamação deve ser feita na hora ao entregador.").setFont(fonteNegrito).setFontSize(9).setFontColor(ColorConstants.RED));

            avisos.addCell(aviso1);

            Cell aviso2 = new Cell();

            aviso2.setBorder(Border.NO_BORDER);

            aviso2.add(new Paragraph("Recebi as mercadorias em perfeitas condições.").setFont(fonteNegrito).setFontSize(9));

            avisos.addCell(aviso2);

            document.add(avisos);

            // ==================================================
            // ASSINATURA
            // ==================================================

            document.add(espaco());

            document.add(new Paragraph("Conferido e Recebido por:").setFont(fonteNormal).setFontSize(9));

            document.add(new Paragraph("____________________________________________________________").setFont(fonteNormal).setFontSize(9));

            Table assinatura = new Table(UnitValue.createPercentArray(new float[]{1, 1}));

            assinatura.setWidth(UnitValue.createPercentValue(100));

            assinatura.setBorder(Border.NO_BORDER);

            Cell dataAssinatura = new Cell();

            dataAssinatura.setBorder(Border.NO_BORDER);

            dataAssinatura.add(new Paragraph("Data: ____/____/________").setFont(fonteNormal).setFontSize(9));

            assinatura.addCell(dataAssinatura);

            Cell rg = new Cell();

            rg.setBorder(Border.NO_BORDER);

            rg.add(new Paragraph("R.G.: __________________________").setFont(fonteNormal).setFontSize(9));

            assinatura.addCell(rg);

            document.add(assinatura);

            // ==================================================
            // OBSERVAÇÃO
            // ==================================================

            document.add(espaco());

            Table observacao = new Table(1);

            observacao.setWidth(UnitValue.createPercentValue(100));

            observacao.setBorder(new SolidBorder(ColorConstants.BLACK, 0.8f));

            Cell obs = new Cell();

            obs.setBorder(Border.NO_BORDER);

            obs.add(new Paragraph("OBS:").setFont(fonteNegrito).setFontSize(9));

            obs.add(new Paragraph(" ").setFontSize(9));

            obs.add(new Paragraph("____________________________________________________________").setFont(fonteNormal).setFontSize(8));

            observacao.addCell(obs);

            document.add(observacao);

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
            throw new RuntimeException("Erro ao gerar PDF do relatório de venda", e);
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

    private void adicionarTotal(
            Table tabela,
            String descricao,
            String valor,
            PdfFont fonteNegrito,
            PdfFont fonteNormal
    ) {

        Cell descricaoCell = new Cell();

        descricaoCell.setBorder(Border.NO_BORDER);

        descricaoCell.setTextAlignment(TextAlignment.RIGHT);

        descricaoCell.add(new Paragraph(descricao).setFont(fonteNegrito).setFontSize(9));

        Cell valorCell = new Cell();

        valorCell.setBorder(Border.NO_BORDER);

        valorCell.setTextAlignment(TextAlignment.RIGHT);

        valorCell.add(new Paragraph(valor).setFont(fonteNormal).setFontSize(9));

        tabela.addCell(descricaoCell);

        tabela.addCell(valorCell);
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

    private String traduzirPagamento(String forma) {

        if (forma == null) {
            return "-";
        }

        return switch (forma) {
            case "CARTAO_CREDITO" -> "Cartão de crédito";
            case "CARTAO_DEBITO" -> "Cartão de débito";
            case "PIX" -> "PIX";
            case "DINHEIRO" -> "Dinheiro";
            case "BOLETO" -> "Boleto";
            case "OUTRO" -> "Outro";
            default -> forma;
        };
    }

    private Paragraph espaco() {

        return new Paragraph(" ")
                .setFontSize(3);
    }
}