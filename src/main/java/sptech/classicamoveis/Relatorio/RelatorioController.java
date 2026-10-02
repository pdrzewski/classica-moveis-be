package sptech.classicamoveis.Relatorio;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sptech.classicamoveis.Relatorio.PdfGenerator.RelatorioPdfGenerator;
import sptech.classicamoveis.Relatorio.PdfGenerator.RelatorioTabelaPrecosPdfGenerator;
import sptech.classicamoveis.Relatorio.PdfGenerator.RelatorioVendedorPdfGenerator;
import sptech.classicamoveis.Relatorio.dto.RelatorioVendaDto;
import sptech.classicamoveis.Relatorio.dto.RelatorioVendaPorVendedorDto;
import sptech.classicamoveis.Relatorio.dto.RelatorioTabelaPrecosDto;

import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/relatorios")
@Tag(
        name = "Relatórios",
        description = "Relatórios de vendas"
)
public class RelatorioController {

    private final RelatorioService relatorioService;
    private final RelatorioPdfGenerator relatorioPdfGenerator;
    private final RelatorioTabelaPrecosPdfGenerator relatorioTabelaPrecosPdfGenerator;
    private final RelatorioVendedorPdfGenerator relatorioVendedorPdfGenerator;

    public RelatorioController(
            RelatorioService relatorioService,
            RelatorioPdfGenerator relatorioPdfGenerator,
            RelatorioTabelaPrecosPdfGenerator relatorioTabelaPrecosPdfGenerator, RelatorioVendedorPdfGenerator relatorioVendedorPdfGenerator) {

        this.relatorioService = relatorioService;
        this.relatorioPdfGenerator = relatorioPdfGenerator;
        this.relatorioTabelaPrecosPdfGenerator = relatorioTabelaPrecosPdfGenerator;
        this.relatorioVendedorPdfGenerator = relatorioVendedorPdfGenerator;
    }

    @GetMapping("/vendas/vendedor/{colaboradorId}")
    public ResponseEntity<RelatorioVendaPorVendedorDto> gerarRelatorioVendaPorVendedor(
            @PathVariable Integer colaboradorId,
            @RequestParam(required = false) LocalDate dataInicio,
            @RequestParam(required = false) LocalDate dataFim) {

        return ResponseEntity.ok(
                relatorioService.gerarRelatorioVendaPorVendedor(
                        colaboradorId,
                        dataInicio,
                        dataFim
                )
        );
    }

    @GetMapping("/vendas/vendedor/{colaboradorId}/pdf")
    public ResponseEntity<byte[]> gerarRelatorioVendaPorVendedorPdf(
            @PathVariable Integer colaboradorId,
            @RequestParam(required = false) LocalDate dataInicio,
            @RequestParam(required = false) LocalDate dataFim) {

        RelatorioVendaPorVendedorDto relatorio =
                relatorioService.gerarRelatorioVendaPorVendedor(
                        colaboradorId,
                        dataInicio,
                        dataFim
                );

        byte[] pdf = relatorioVendedorPdfGenerator.gerarPdf(relatorio);

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_PDF);

        headers.setContentDisposition(
                ContentDisposition
                        .attachment()
                        .filename(montarNomeArquivo(
                                        "Relatorio-vendas", relatorio.getVendedorNome())).build()
        );

        headers.setContentLength(pdf.length);

        return ResponseEntity.ok().headers(headers).body(pdf);
    }

    @GetMapping("/produtos/tabela-precos/{estabelecimentoId}")
    public ResponseEntity<RelatorioTabelaPrecosDto> gerarTabelaPrecos(
            @PathVariable Integer estabelecimentoId,
            @RequestParam(required = false) Long fornecedorId) {

        return ResponseEntity.ok(
                relatorioService.gerarTabelaPrecos(
                        estabelecimentoId,
                        fornecedorId
                )
        );
    }

    @GetMapping("/produtos/tabela-precos/{estabelecimentoId}/pdf")
    public ResponseEntity<byte[]> gerarTabelaPrecosPdf(
            @PathVariable Integer estabelecimentoId,
            @RequestParam(required = false) Long fornecedorId) {

        RelatorioTabelaPrecosDto relatorio = relatorioService.gerarTabelaPrecos(estabelecimentoId, fornecedorId);

        byte[] pdf = relatorioTabelaPrecosPdfGenerator.gerarPdf(relatorio);

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_PDF);

        headers.setContentDisposition(
                ContentDisposition
                        .attachment()
                        .filename(montarNomeArquivo(
                                        "Tabela-precos-estoque",
                                        relatorio.getEstabelecimento())).build()
        );

        headers.setContentLength(pdf.length);

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(pdf);
    }

    @GetMapping("/venda/{movimentacaoId}")
    public ResponseEntity<RelatorioVendaDto> gerarRelatorioVenda(
            @PathVariable Integer movimentacaoId) {

        return ResponseEntity.ok(relatorioService.gerarRelatorioVenda(movimentacaoId));
    }

    @GetMapping("/venda/{movimentacaoId}/pdf")
    public ResponseEntity<byte[]> gerarRelatorioVendaPdf(
            @PathVariable Integer movimentacaoId) {

        RelatorioVendaDto relatorio = relatorioService.gerarRelatorioVenda(movimentacaoId);

        byte[] pdf = relatorioPdfGenerator.gerarPdf(relatorio);

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_PDF);

        headers.setContentDisposition(
                ContentDisposition
                        .attachment()
                        .filename("pedido-" + movimentacaoId + ".pdf").build());

        headers.setContentLength(pdf.length);

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(pdf);
    }

    private static final DateTimeFormatter FORMATO_DATA_ARQUIVO = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private String montarNomeArquivo(String prefixo, String nome) {

        String nomeLimpo = "";

        if (nome != null && !nome.isBlank()) {
            nomeLimpo = Normalizer
                    .normalize(nome, Normalizer.Form.NFD)
                    .replaceAll("\\p{M}", "")
                    .replaceAll("[^A-Za-z0-9]+", "-")
                    .replaceAll("^-+|-+$", "");
        }

        String data = LocalDate.now().format(FORMATO_DATA_ARQUIVO);

        return prefixo + (nomeLimpo.isEmpty() ? "" : "-" + nomeLimpo) + "-" + data + ".pdf";
    }
}