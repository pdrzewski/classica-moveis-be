package sptech.classicamoveis.Relatorio;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioController {

    private final RelatorioService relatorioService;
    private final RelatorioPdfGenerator relatorioPdfGenerator;

    // Construtor manual para injetar as dependências sem depender do Lombok
    public RelatorioController(RelatorioService relatorioService, RelatorioPdfGenerator relatorioPdfGenerator) {
        this.relatorioService = relatorioService;
        this.relatorioPdfGenerator = relatorioPdfGenerator;
    }

    @GetMapping("/vendas-por-fornecedor")
    public ResponseEntity<List<RelatorioVendaItemDto>> relatorioVendasPorFornecedor(
            @RequestParam Integer fornecedorId,
            @RequestParam(required = false) Integer idLoja) {
        return ResponseEntity.ok(relatorioService.relatorioVendasPorFornecedor(fornecedorId, idLoja));
    }

    @GetMapping("/vendas-por-produto")
    public ResponseEntity<List<RelatorioVendasPorProdutoDto>> relatorioVendasPorProduto(
            @RequestParam(required = false) Integer categoriaId,
            @RequestParam(required = false) Integer idLoja,
            @RequestParam(required = false) List<Integer> produtoIds,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim) {
        return ResponseEntity.ok(relatorioService.relatorioVendasPorProduto(categoriaId, idLoja, produtoIds, dataInicio, dataFim));
    }

    @GetMapping(value = "/vendas-por-fornecedor/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> relatorioVendasPorFornecedorPdf(
            @RequestParam Integer fornecedorId,
            @RequestParam(required = false) Integer idLoja) {
        List<RelatorioVendaItemDto> dados = relatorioService.relatorioVendasPorFornecedor(fornecedorId, idLoja);
        byte[] pdf = relatorioPdfGenerator.gerarRelatorioVendasPorFornecedor(dados);
        return respostaPdf(pdf, "relatorio-vendas-por-fornecedor.pdf");
    }

    @GetMapping(value = "/vendas-por-produto/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> relatorioVendasPorProdutoPdf(
            @RequestParam(required = false) Integer categoriaId,
            @RequestParam(required = false) Integer idLoja,
            @RequestParam(required = false) List<Integer> produtoIds,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim) {
        List<RelatorioVendasPorProdutoDto> dados =
                relatorioService.relatorioVendasPorProduto(categoriaId, idLoja, produtoIds, dataInicio, dataFim);
        byte[] pdf = relatorioPdfGenerator.gerarRelatorioVendasPorProduto(dados);
        return respostaPdf(pdf, "relatorio-vendas-por-produto.pdf");
    }

    private ResponseEntity<byte[]> respostaPdf(byte[] pdf, String nomeArquivo) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nomeArquivo + "\"")
                .body(pdf);
    }
}